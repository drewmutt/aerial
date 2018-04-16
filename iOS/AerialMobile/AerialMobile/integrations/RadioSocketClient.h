//
// Created by Andrew Simmons on 6/30/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface RadioSocketClient : NSObject <NSStreamDelegate>

- (void)initNetworkCommunication;

- (void)getFrequency;
@end