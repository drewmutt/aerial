//
//  StationsTableViewController.m
//  AerialMobile
//
//  Created by Andrew Simmons on 6/22/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import "StationsTableViewController.h"
#import "StationDetailsViewController.h"
#import "StationManager.h"
#import "StationTableViewCell.h"
#import "RadioConnector.h"

@interface StationsTableViewController ()


@end

@implementation StationsTableViewController {
    NSTimer *_timer;
}


- (void)connectedToRadio {

}

- (void)gotStations {

}

- (void)gotFrequency:(NSNumber *)frequency {
//    NSLog(@"%@", frequency);
    Station *closestToFrequency = [[StationManager instance] getStationClosestToFrequency:frequency.unsignedIntegerValue];
    NSUInteger closestIndex = [[[StationManager instance] stations] indexOfObject:closestToFrequency];
    UITableView *view = (UITableView *) self.view;
	NSIndexPath *path = [NSIndexPath indexPathForRow:closestIndex inSection:0];

	StationTableViewCell *cell = (StationTableViewCell *) [view cellForRowAtIndexPath:path];
	for(StationTableViewCell *cell2 in view.visibleCells)
	{
		[cell2 displayAsNormal];
	}

	[cell displayAsTuned];
    [_timer invalidate];
    _timer = nil;
    _timer = [NSTimer scheduledTimerWithTimeInterval:.5 target:self selector:@selector(getFreqTimerElapsed:) userInfo:nil repeats:YES];
    [[NSRunLoop mainRunLoop] addTimer:_timer forMode:NSRunLoopCommonModes];
}


- (void) getFreqTimerElapsed:(NSTimer *) timer
{
    [RadioConnector instance].delegate = self;
    [[RadioConnector instance] getCurrentFrequency];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    [self getFreqTimerElapsed:nil];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
#warning Incomplete method implementation.
    // Return the number of rows in the section.
    return [[StationManager instance] stations].count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    StationTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"stationCell" forIndexPath:indexPath];
    NSMutableArray *allStations = [StationManager instance].stations;

    [cell populateWithStation:allStations[(NSUInteger) indexPath.row]];

    // Configurethe cell...
    
    return cell;
}


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Navigation

- (void)viewWillDisappear:(BOOL)animated {
    [_timer invalidate];
    _timer = nil;
        [super viewWillDisappear:animated];

}

- (void)viewWillAppear:(BOOL)animated
{
    [_timer invalidate];
//    _timer = [NSTimer scheduledTimerWithTimeInterval:3 target:self selector:@selector(getFreqTimerElapsed:) userInfo:nil repeats:YES];
    UITableView *view = (UITableView *) self.view;
    [view reloadData];

    [self getFreqTimerElapsed:nil];
    [super viewWillAppear:animated];


}

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([segue.identifier isEqualToString:@"showStationDetails"])
    {
        NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        StationDetailsViewController *destViewController = segue.destinationViewController;
        destViewController.station = [[StationManager instance] stations] [(NSUInteger) indexPath.row];
    }
}

@end
