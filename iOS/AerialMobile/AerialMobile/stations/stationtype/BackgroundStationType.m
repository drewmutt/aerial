//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "BackgroundStationType.h"


@implementation BackgroundStationType
{

}

- (void) applyStyleToStationDot:(UIImageView *)image
{
	double scaleAlpha = .3;
	image.alpha = (CGFloat) scaleAlpha;
	image.frame = CGRectMake(image.frame.origin.x, image.frame.origin.y, (CGFloat) (image.frame.size.width * scaleAlpha), (CGFloat) (image.frame.size.width * scaleAlpha));
}


@end