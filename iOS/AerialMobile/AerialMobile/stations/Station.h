//
// Created by Andrew Simmons on 6/23/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IStationType;


@interface Station : NSObject {
}

@property(nonatomic) NSNumber *frequency;
@property(nonatomic) NSString *stationName;


@property(nonatomic, copy) NSString *stationTypeString;
@property(nonatomic, readonly) NSObject <IStationType> *stationType;
@end