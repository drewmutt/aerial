//
// Created by Andrew Simmons on 6/23/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IRadioConnectorDelegate <NSObject>
-(void) connectedToRadio;
-(void) gotStations;

- (void)gotFrequency:(NSNumber *)frequency;
@end