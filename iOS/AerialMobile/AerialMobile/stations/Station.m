//
// Created by Andrew Simmons on 6/23/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "Station.h"
#import "IStationType.h"
#import "UserStationType.h"
#import "BackgroundStationType.h"
#import "PresetStationType.h"


@implementation Station {

}


-(NSObject <IStationType> *) stationType
{
	if([_stationTypeString isEqualToString:@"SoundAssetRadioStation"])
		return [PresetStationType new];
	else if([_stationTypeString isEqualToString:@"ToneRadioStation"] || [_stationTypeString isEqualToString:@"BeepingToneRadioStation"])
		return [BackgroundStationType new];
	else if([_stationTypeString isEqualToString:@"SoundCloudRadioStation"])
		return [UserStationType new];

	return nil;
}

@end