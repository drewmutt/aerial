//
//  StationTableViewCell.h
//  AerialMobile
//
//  Created by Andrew Simmons on 6/22/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Station;

@interface StationTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *_stationNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *_stationFreqLabel;
@property (weak, nonatomic) IBOutlet UIView *highlightView;
@property (strong, nonatomic) IBOutlet UIImageView *tunedImage;

- (void)populateWithStation:(Station *)station;

- (void)displayAsTuned;

- (void)displayAsNormal;
@end
