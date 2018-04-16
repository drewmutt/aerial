//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "Range.h"


@implementation Range
{
	double _min;
	double _max;
}


- (instancetype)initWithMin:(double)min max:(double)max
{
	self = [super init];
	if (self)
	{
		_min = min;
		_max = max;
	}

	return self;
}

+ (instancetype)rangeWithMin:(double)min max:(double)max
{
	return [[self alloc] initWithMin:min max:max];
}

- (double)span
{
	return _max - _min;
}


-(double) getValueAtPercentage:(double) percentage
{
	double value = self.span * percentage + _min;
	return value;
}

-(double) getPercentageAtValue:(double) value
{
	return (value - _min) / self.span;
}



@end