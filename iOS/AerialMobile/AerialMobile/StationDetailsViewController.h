//
//  StationDetailsViewController.h
//  AerialMobile
//
//  Created by Andrew Simmons on 6/23/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Station;

@interface StationDetailsViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>

@property(nonatomic, strong) Station* station;
@end
