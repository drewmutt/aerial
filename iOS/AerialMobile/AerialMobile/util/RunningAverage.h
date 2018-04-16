//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface RunningAverage : NSObject
- (instancetype)initWithSampleSize:(unsigned int)sampleSize;

- (void)addSample:(NSNumber *)sample;

- (NSNumber *)average;
@end