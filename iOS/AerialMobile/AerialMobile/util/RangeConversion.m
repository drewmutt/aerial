//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "RangeConversion.h"
#import "Range.h"


@implementation RangeConversion
{
	Range *_keyRange;
	Range *_valueRange;
	
}


- (instancetype)initWithKeyRange:(Range *)keyRange valueRange:(Range *)valueRange
{
	self = [super init];
	if (self)
	{
		_keyRange = keyRange;
		_valueRange = valueRange;
	}

	return self;
}

+ (instancetype)conversionWithKeyRange:(Range *)keyRange valueRange:(Range *)valueRange
{
	return [[self alloc] initWithKeyRange:keyRange valueRange:valueRange];
}

+ (instancetype)conversionWithKeyMin:(double)keyMin keyMax:(double)keyMax valueMin:(double)valueMin valueMax:(double) valueMax
{
	return [[self alloc] initWithKeyRange:[Range rangeWithMin:keyMin max:keyMax] valueRange:[Range rangeWithMin:valueMin max:valueMax]];
}

+ (instancetype)conversionWithKeyRange:(Range *)keyRange valueMin:(double)valueMin valueMax:(double) valueMax
{
	return [[self alloc] initWithKeyRange:keyRange valueRange:[Range rangeWithMin:valueMin max:valueMax]];
}

+ (instancetype)conversionWithKeyMin:(double)keyMin keyMax:(double)keyMax valueRange:(Range *) valueRange
{
	return [[self alloc] initWithKeyRange:[Range rangeWithMin:keyMin max:keyMax] valueRange:valueRange];
}

-(double) getValueAtKey:(double) value
{
	double keyPercentage = [_keyRange getPercentageAtValue:value];
	double outValue = [_valueRange getValueAtPercentage:keyPercentage];
	return outValue;
}


-(double) getKeyAtValue:(double) value
{
	double keyPercentage = [_valueRange getPercentageAtValue:value];
	double outValue = [_keyRange getValueAtPercentage:keyPercentage];
	return outValue;
}



@end