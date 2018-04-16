//
//  WaitingToConnectViewController.m
//  AerialMobile
//
//  Created by Andrew Simmons on 6/23/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import "WaitingToConnectViewController.h"
#import "RadioConnector.h"

@interface WaitingToConnectViewController ()
@property (weak, nonatomic) IBOutlet UILabel *mainLabel;

@end

@implementation WaitingToConnectViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.

    [RadioConnector instance].delegate = self;
    [[RadioConnector instance] listenForBroadcastedIP];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)connectedToRadio {
    _mainLabel.text = @"Gettin them stations..";
    [[RadioConnector instance] getStations];
}

- (void)gotStations {
    _mainLabel.text = @"Got stations..";
    [self performSegueWithIdentifier:@"connected" sender:self];
}

- (void)gotFrequency:(NSNumber *)frequency {

}



/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
