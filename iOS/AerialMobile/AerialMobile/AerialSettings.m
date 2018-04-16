//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "AerialSettings.h"
#import "Range.h"


@implementation AerialSettings
{

}

- (NSTimeInterval)tunerFrequencyUpdateInSec
{
	return .2;
}

//Higher values are slower response to freq change, lower values are jittery
- (unsigned int) tunerRunningAverageSampleSize
{
	return 10;
}

-(int) getMinStationFrequency
{
	return 500;
}

-(int) getMaxStationFrequency
{
	return 1650;
}


-(Range *) getStationFrequencyRange
{
	return [Range rangeWithMin:[self getMinStationFrequency] max:[self getMaxStationFrequency]];
}

+ (AerialSettings *)instance
{
	static AerialSettings *_instance = nil;

	@synchronized (self)
	{
		if (_instance == nil)
		{
			_instance = [[self alloc] init];
		}
	}
	return _instance;
}

@end