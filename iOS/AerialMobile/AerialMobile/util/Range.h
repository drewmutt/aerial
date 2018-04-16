//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Range : NSObject
@property(nonatomic, readonly) double min;
@property(nonatomic, readonly) double max;

- (instancetype)initWithMin:(double)min max:(double)max;

- (double)getValueAtPercentage:(double)percentage;

- (double)getPercentageAtValue:(double)value;

+ (instancetype)rangeWithMin:(double)min max:(double)max;

@property(nonatomic, readonly) double span;
@end