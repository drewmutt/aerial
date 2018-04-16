//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "RunningAverage.h"


@implementation RunningAverage
{
	NSMutableArray *_samples;
	unsigned int _sampleSize;
}

- (instancetype)initWithSampleSize:(unsigned int)sampleSize
{
	self = [super init];
	if (self)
	{
		_sampleSize = sampleSize;
		_samples = [NSMutableArray new];
	}

	return self;
}


-(void) addSample:(NSNumber *) sample
{
	if(_samples.count >= _sampleSize)
	{
		[_samples removeLastObject];
	}

	[_samples insertObject:sample atIndex:0];

//	for (int i = 0; i < _samples.count; ++i)
//	{
//		NSLog(@"%d: %@", i, _samples[i]);
//	}
//	NSLog(@"-----");
}


-(NSNumber *) average
{
	double sum = 0;

	for (NSNumber *number in _samples)
	{
		sum += [number doubleValue];
	}
	return @(sum / ((double) _samples.count));
}
@end