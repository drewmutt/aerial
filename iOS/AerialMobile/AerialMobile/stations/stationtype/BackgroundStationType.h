//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IStationType.h"


@interface BackgroundStationType : NSObject <IStationType>
- (void)applyStyleToStationDot:(UIImageView *)image;
@end