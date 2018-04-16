//
//  StationTableViewCell.m
//  AerialMobile
//
//  Created by Andrew Simmons on 6/22/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import "StationTableViewCell.h"
#import "Station.h"

@implementation StationTableViewCell

- (void)awakeFromNib {
    // Initialization code
	_tunedImage.hidden = true;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void) populateWithStation:(Station *)station {
    __stationFreqLabel.text = [NSString stringWithFormat:@"%@", station.frequency];
    __stationNameLabel.text = station.stationName;
}

- (void)displayAsTuned {

	_tunedImage.hidden = false;
    __stationFreqLabel.textColor = [UIColor whiteColor];

}

- (void)displayAsNormal {
	_tunedImage.hidden = true;
    __stationFreqLabel.textColor = [UIColor blackColor];
}
@end
