//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Range;


@interface RangeConversion : NSObject
- (instancetype)initWithKeyRange:(Range *)keyRange valueRange:(Range *)valueRange;

- (double)getKeyAtValue:(double)value;

+ (instancetype)conversionWithKeyRange:(Range *)keyRange valueMin:(double)valueMin valueMax:(double)valueMax;

+ (instancetype)conversionWithKeyMin:(double)keyMin keyMax:(double)keyMax valueRange:(Range *)valueRange;

- (double)getValueAtKey:(double)value;

+ (instancetype)conversionWithKeyRange:(Range *)keyRange valueRange:(Range *)valueRange;

+ (instancetype)conversionWithKeyMin:(double)keyMin keyMax:(double)keyMax valueMin:(double)valueMin valueMax:(double)valueMax;
@end