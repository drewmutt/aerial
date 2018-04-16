//
//  SelectSoundCloudPlaylistViewController.h
//  AerialMobile
//
//  Created by Andrew Simmons on 6/24/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Station;

@interface SelectSoundCloudPlaylistViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITextField *soundcloudUserName;
@property (weak, nonatomic) IBOutlet UITableView *playlistTableView;

@property(nonatomic, strong) Station *station;
@end
