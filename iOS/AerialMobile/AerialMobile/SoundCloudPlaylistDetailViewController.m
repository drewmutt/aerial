//
//  SoundCloudPlaylistDetailViewController.m
//  AerialMobile
//
//  Created by Andrew Simmons on 6/24/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import "SoundCloudPlaylistDetailViewController.h"
#import "SoundCloudPlaylist.h"
#import "Station.h"
#import "RadioConnector.h"

@interface SoundCloudPlaylistDetailViewController ()

@end

@implementation SoundCloudPlaylistDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)addPlaylistTouched:(id)sender
{

    [[RadioConnector instance] populateStationWithSoundcloudPlaylistWithFreq:[_station.frequency intValue] soundCloudPlaylistId:[_playlist.id intValue]];
    SoundCloudPlaylistDetailViewController *controller = self;
    [RadioConnector instance].delegate = controller;
    [[RadioConnector instance] getStations];
}

- (void)connectedToRadio {

}

- (void)gotStations {
    [self.navigationController popToRootViewControllerAnimated:YES];
}

- (void)gotFrequency:(NSNumber *)frequency {

}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    Get the new view controller using [segue destinationViewController].
    Pass the selected object to the new view controller.
}
*/

@end
