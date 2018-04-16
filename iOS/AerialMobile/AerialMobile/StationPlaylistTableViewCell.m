//
//  StationPlaylistTableViewCell.m
//  AerialMobile
//
//  Created by Andrew Simmons on 6/26/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import "StationPlaylistTableViewCell.h"

@implementation StationPlaylistTableViewCell
{
    __weak IBOutlet UILabel *titleLabel;
    __weak IBOutlet UILabel *artistLabel;
}
- (void)awakeFromNib {
    // Initialization code
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void) setTitle:(NSString *) title artist:(NSString *) artist
{
	titleLabel.text = title;
	artistLabel.text = artist;
}

@end
