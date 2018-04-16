//
//  StationDetailsViewController.m
//  AerialMobile
//
//  Created by Andrew Simmons on 6/23/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import "StationDetailsViewController.h"
#import "Station.h"
#import "SelectSoundCloudPlaylistViewController.h"
#import "RadioConnector.h"
#import "StationPlaylistTableViewCell.h"
#import "ITrack.h"

@interface StationDetailsViewController ()
@property (weak, nonatomic) IBOutlet UILabel *stationNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *stationFreqLabel;
@property (weak, nonatomic) IBOutlet UITableView *stationPlaylistTableView;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *activityIndicator;

@end

@implementation StationDetailsViewController
{
	NSArray *_playlist;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _stationFreqLabel.text = _station.frequency.stringValue;
    _stationNameLabel.text = _station.stationName;

	_stationPlaylistTableView.delegate = self;
	_stationPlaylistTableView.dataSource = self;

	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(gotStationPlaylist:) name:NOTIFICATION_GOT_PLAYLISTS_FOR_STATION object:nil];
	[[RadioConnector instance] getTrackPlaylistAtFrequency:[_station.frequency intValue]];


}

- (void)gotStationPlaylist:(NSNotification*)notification
{
	NSArray *playlist = notification.userInfo[@"playlist"];
	_playlist = playlist;
	[_stationPlaylistTableView reloadData];

	[[NSNotificationCenter defaultCenter] removeObserver:self];

	[_activityIndicator stopAnimating];
	[_activityIndicator removeFromSuperview];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([segue.identifier isEqualToString:@"viewSCPlaylists"])
    {
        SelectSoundCloudPlaylistViewController *destViewController = segue.destinationViewController;
        destViewController.station = _station; 
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	if(_playlist == nil)
		return 0;
	else
		return _playlist.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
	if(_playlist == nil)
		return nil;

	StationPlaylistTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"playlistCell" forIndexPath:indexPath];
	NSObject <ITrack> * track = _playlist[(NSUInteger) indexPath.row];
	[cell setTitle:track.title artist:track.artist];
	// Configurethe cell...
    return cell;
}


@end
