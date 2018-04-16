//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "StationFrequencyLabelView.h"


@implementation StationFrequencyLabelView
{
	UILabel *_mainLabel;
	UILabel *_shadowLabel;
}

- (instancetype)init
{
	self = [super init];
	if (self)
	{
		int labelWidth = 60;
		int labelHeight = 30;
		int fontSize = 20;
		int shadowDistance = 1;

		UILabel *newLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, labelWidth, labelHeight)];
		newLabel.textAlignment = NSTextAlignmentCenter;
		newLabel.font = [UIFont fontWithName:@"AvenirNextCondensed-DemiBold" size:fontSize];
		newLabel.text = @"150";
		newLabel.textColor = [UIColor whiteColor];
		_mainLabel = newLabel;


		UILabel *shadowLabel = [[UILabel alloc] initWithFrame:CGRectMake(shadowDistance, shadowDistance, labelWidth, labelHeight)];
		shadowLabel.textAlignment = NSTextAlignmentCenter;
		shadowLabel.font = [UIFont fontWithName:@"AvenirNextCondensed-DemiBold" size:fontSize];
		shadowLabel.text = @"150";
		shadowLabel.textColor = [UIColor colorWithRed:.48 green:.48 blue:.48 alpha:1];

		_shadowLabel = shadowLabel;

		[self addSubview:shadowLabel];
		[self addSubview:_mainLabel];

		self.bounds = CGRectMake(0, 0, labelWidth + shadowDistance, labelHeight + shadowDistance);
	}

	return self;
}

-(void) setFrequency:(int) frequency
{
	_mainLabel.text = [NSString stringWithFormat:@"%d", frequency];
	_shadowLabel.text = [NSString stringWithFormat:@"%d", frequency];
}

- (int)getFrequency
{
	return [_mainLabel.text intValue];
}

@end