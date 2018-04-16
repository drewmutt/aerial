//
//  SelectSoundCloudPlaylistViewController.m
//  AerialMobile
//
//  Created by Andrew Simmons on 6/24/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import <AVKit/AVKit.h>
#import <AVFoundation/AVFoundation.h>
#import "SelectSoundCloudPlaylistViewController.h"
#import "SoundCloudConnector.h"
#import "PlaylistTableViewCell.h"
#import "SoundCloudPlaylist.h"
#import "SoundCloudPlaylistDetailViewController.h"
#import "RadioConnector.h"
#import "NSString+ObjectiveSugar.h"
#import "Station.h"

@interface SelectSoundCloudPlaylistViewController ()
{
    
}
@end

@implementation SelectSoundCloudPlaylistViewController {
    NSArray *_playlists;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    _playlistTableView.delegate = self;
    _playlistTableView.dataSource = self;
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)fetchPlaylistTouched:(id)sender
{
    _playlists = [[SoundCloudConnector instance] getPlaylistsForUser:_soundcloudUserName.text];
    [_playlistTableView reloadData];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if(_playlists == nil)
        return 0;
    else
        return _playlists.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if(_playlists == nil)
        return nil;

//    PlaylistTableViewCell *cell = [PlaylistTableViewCell new];
    PlaylistTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"playlistCell" forIndexPath:indexPath];
    SoundCloudPlaylist *playlist = _playlists[(NSUInteger) indexPath.row];
    cell.playlistNameLabel.text = playlist.title;
    return cell;
}



#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([segue.identifier isEqualToString:@"showPlaylistDetail"])
    {
        NSIndexPath *indexPath = [_playlistTableView indexPathForSelectedRow];
        SoundCloudPlaylistDetailViewController *destViewController = segue.destinationViewController;
        SoundCloudPlaylist *playlist = _playlists[(NSUInteger) indexPath.row];
        destViewController.playlist = playlist;
        destViewController.station = _station;
    }

    if ([segue.identifier isEqualToString:@"player"])
    {
        AVPlayerViewController* destViewController = segue.destinationViewController;
        NSString *string = [RadioConnector instance].radioURL;
        NSString *urlWithoutPort = [string substringToIndex:string.length - 5];
        NSString *host = [NSString stringWithFormat:@"http://%@:8000/raspi", urlWithoutPort];
        NSURL *url = [[NSURL alloc] initWithString:host];
        destViewController.player = [AVPlayer playerWithURL:url];
    }
}


@end
