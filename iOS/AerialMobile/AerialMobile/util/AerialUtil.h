//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Range;

#define RADIANS_TO_DEGREES(radians) ((radians) * (180.0 / M_PI))
#define DEGREES_TO_RADIANS(angle) ((angle) / 180.0 * M_PI)

@interface AerialUtil : NSObject
@end