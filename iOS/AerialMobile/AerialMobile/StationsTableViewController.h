//
//  StationsTableViewController.h
//  AerialMobile
//
//  Created by Andrew Simmons on 6/22/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IRadioConnectorDelegate.h"

@interface StationsTableViewController : UITableViewController <IRadioConnectorDelegate> {
}

- (void)getFreqTimerElapsed:(NSTimer *)timer;
@end
