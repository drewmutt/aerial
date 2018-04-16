//
//  TunerViewController.m
//  AerialMobile
//
//  Created by Andrew Simmons on 6/29/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import "TunerViewController.h"
#import "StationFrequencyLabelView.h"
#import "AerialUtil.h"
#import "RangeConversion.h"
#import "Range.h"
#import "RadioConnector.h"
#import "StationManager.h"
#import "Station.h"
#import "AerialSettings.h"
#import "RunningAverage.h"
#import "BackgroundStationType.h"
#import "RadioSocketClient.h"

@interface TunerViewController ()
@property (strong, nonatomic) IBOutlet UIView *dialView;
@property (strong, nonatomic) IBOutlet UIView *currentStationIndicator;
@property (strong, nonatomic) IBOutlet UILabel *currentStationIndicatorLabel;
@property (strong, nonatomic) IBOutlet UIImageView *moreStationsIndicatorLeft;
@property (strong, nonatomic) IBOutlet UIImageView *moreStationsIndicatorRight;
@property (strong, nonatomic) IBOutlet UIView *connectingToRadioView;
@property (strong, nonatomic) IBOutlet UILabel *connectingToRadioLabel;
@property (strong, nonatomic) IBOutlet UIImageView *currentStationIndicatorShine;

@end

@implementation TunerViewController
{
	RangeConversion *_freqToDegreesConversion;
	double _currentDialRotationInDeg;
	NSMutableArray *_stationFreqLabels;
	NSTimer *_timer;
	int _testVal;
	double _currentFrequency;
	CGPoint _dialCenterPoint;
	NSMutableArray *_tuningDialDots;
	RunningAverage *_frequencyRunningAverage;
}


-(CGPoint)getCenterOfDialPoint
{
	return CGPointMake(_dialView.frame.origin.x + _dialView.frame.size.width / 2, _dialView.frame.origin.y + _dialView.frame.size.height / 2);
}


- (void)viewDidLoad {
    [super viewDidLoad];

	_stationFreqLabels = [NSMutableArray new];

	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(connectedToRadio:) name:NOTIFICATION_CONNECTED_TO_RADIO object:nil];
 	[[RadioConnector instance] listenForBroadcastedIP];

	_currentStationIndicator.hidden = YES;

	_dialCenterPoint = [self getCenterOfDialPoint];
	_frequencyRunningAverage  = [[RunningAverage alloc] initWithSampleSize:[AerialSettings instance].tunerRunningAverageSampleSize];
}


- (void)connectedToRadio:(NSNotification *)notification
{
	[[NSNotificationCenter defaultCenter] removeObserver:self name:NOTIFICATION_CONNECTED_TO_RADIO object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(gotStations:)
												 name:NOTIFICATION_GOT_STATIONS
											   object:nil];
	_connectingToRadioLabel.text = @"Connected. Getting stations..";
	NSLog(@"Connected to radio..");
	[[RadioConnector instance] getStations];


}

- (void)gotStations:(NSNotification *) notification
{
	NSLog(@"Got stations...");
	[[NSNotificationCenter defaultCenter] removeObserver:self name:NOTIFICATION_GOT_STATIONS object:nil];
	_connectingToRadioLabel.text = @"Got stations..";
	[self setupStationLabels];
	_connectingToRadioView.hidden = true;
	NSLog(@"Listening for freq changes..");
	
	[self getFreqTimerElapsed:nil];
	_testVal = 500;
    [[RadioConnector instance].socketClient initNetworkCommunication];
}

- (void)gotCurrentFrequency:(NSNotification *) notification
{
	NSNumber *freq = (NSNumber *) notification.userInfo[@"frequency"];
	[_frequencyRunningAverage addSample:freq];

	[self setDialToFrequency:(int) [[_frequencyRunningAverage average] doubleValue]];

	[_timer invalidate];
	 _timer = nil;

/*
	dispatch_async(dispatch_get_main_queue(), ^{
		_timer = [NSTimer scheduledTimerWithTimeInterval:[AerialSettings instance].tunerFrequencyUpdateInSec target:self selector:@selector(getFreqTimerElapsed:) userInfo:nil repeats:NO];
	});
*/
}


- (void)getFreqTimerElapsed:(NSTimer *) timer
{
	[[NSNotificationCenter defaultCenter] removeObserver:self name:NOTIFICATION_GOT_CURRENT_FREQUENCY object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self 
											 selector:@selector(gotCurrentFrequency:) 
												 name:NOTIFICATION_GOT_CURRENT_FREQUENCY 
											   object:nil];

	[[RadioConnector instance] getCurrentFrequency];
}


-(void) setupStationLabels
{
	Range *freqRange = [[AerialSettings instance] getStationFrequencyRange];
	_freqToDegreesConversion = [RangeConversion conversionWithKeyRange:freqRange valueRange:[Range rangeWithMin:320 max:0]];

	for(double frequency = freqRange.min; frequency <= freqRange.max; frequency +=50)
	{
		StationFrequencyLabelView *newFreqLabel = [StationFrequencyLabelView new];
		[newFreqLabel setFrequency:(int) frequency];
		[self.view addSubview:newFreqLabel];
		[_stationFreqLabels addObject:newFreqLabel];
	}

	[self updateStationFreqLabels];



	_tuningDialDots = [NSMutableArray new];

	NSMutableArray *allStations = [[StationManager instance] stations];

	//Add station dots
	for (Station *station in allStations)
	{
		UIImageView *imageView = [[UIImageView new] initWithImage:[UIImage imageNamed:@"tuner-tuningWheelDot"]];
		
		double degrees = [self convertFrequencyToLabelDegress:[station.frequency doubleValue]];
		
		CGPoint point = [self getPointOffsetFromDialAtDegrees:degrees distanceFromCenter:249 offsetOnly:YES];
		CGPoint adjPoint = CGPointMake(point.x + _dialView.frame.size.width / 2, point.y + _dialView.frame.size.height / 2);

        imageView.frame = CGRectMake(adjPoint.x - imageView.frame.size.width /2, adjPoint.y - imageView.frame.size.height /2, imageView.image.size.width, imageView.image.size.height) ;//imageView.frame.size.width, imageView.frame.size.height);

        NSObject <IStationType> *stationType = [station stationType];
        [stationType applyStyleToStationDot:imageView];

        [_dialView addSubview:imageView];


	}


	_currentStationIndicator.hidden = NO;
}

-(void) updateStationFreqLabels
{
	RangeConversion *alphaRange = [RangeConversion conversionWithKeyMin:0 keyMax:200 valueMin:1 valueMax:0];

	for (StationFrequencyLabelView *freqLabel in _stationFreqLabels)
	{
		int frequency = freqLabel.getFrequency;
		double degrees = [self convertFrequencyToLabelDegress:frequency];
		CGPoint point = [self getPointOffsetFromDialAtDegrees:degrees];
		point.x = point.x - freqLabel.bounds.size.width / 2;
		freqLabel.frame = CGRectMake(point.x, point.y, freqLabel.frame.size.width, freqLabel.frame.size.height);

		double distanceFromCenterFreq = fabs(_currentFrequency - ((double) frequency));
		double alpha = [alphaRange getValueAtKey:distanceFromCenterFreq];
		if (alpha > 1)
			alpha = 1;
		else if (alpha < .2)
			alpha = 0;

		freqLabel.alpha = alpha;
		if(freqLabel.alpha == 0)
            freqLabel.hidden = YES;
		else
            freqLabel.hidden = NO;
	}

	Station *closestStation = nil;
	int closestFreqDist = 1000000;

	NSMutableArray *allStations = [[StationManager instance] stations];

	for (Station *station in allStations)
	{
		double distanceFromCenterFreq = fabs(_currentFrequency - station.frequency.doubleValue);
		if (distanceFromCenterFreq < closestFreqDist)
		{
			closestFreqDist = (int) distanceFromCenterFreq;
			closestStation = station;
		}
	}

	if(closestFreqDist < 10)
	{
		_currentStationIndicator.hidden = NO;
		StationFrequencyLabelView *freqLabel = [self getFrequencyLabelForFrequency:closestStation.frequency.intValue];
		freqLabel.hidden = YES;
		[self placeTunedIndicatorAtFrequency:_currentFrequency displayFrequency:_currentFrequency];
	}
	else
	{
		_currentStationIndicator.hidden = YES;
	}


}

-(StationFrequencyLabelView *) getFrequencyLabelForFrequency:(int) freq
{
	for(StationFrequencyLabelView *freqLabel in _stationFreqLabels)
	{
		if(freqLabel.getFrequency == freq)
			return freqLabel;
	}
	return nil;
}

- (void)placeTunedIndicatorAtFrequency:(int)freq displayFrequency:(double)frequency
{
	_currentStationIndicatorLabel.text = [NSString stringWithFormat:@"%d", (int)frequency];
}

-(void) setDialToFrequency:(double) frequency
{
	double rotation = [self convertFrequencyToLabelDegress:frequency];
	[self setDialRotation:rotation];
	_currentFrequency = frequency;
}

-(void) setDialRotation:(double) degrees
{
	double rads = DEGREES_TO_RADIANS(degrees);
	CGAffineTransform transform = CGAffineTransformRotate(CGAffineTransformIdentity, rads);
	self.dialView.transform = transform;
	_currentDialRotationInDeg = degrees;
	[self updateStationFreqLabels];
	
}
-(double) convertFrequencyToLabelDegress:(double) freq
{
	return [_freqToDegreesConversion getValueAtKey:freq];
}

-(CGPoint) getPointOffsetFromDialAtDegrees:(double) degrees
{
	return [self getPointOffsetFromDialAtDegrees:degrees distanceFromCenter:300 offsetOnly:NO];
}

-(CGPoint) getPointOffsetFromDialAtDegrees:(double) degrees distanceFromCenter:(double) dist
{
	return [self getPointOffsetFromDialAtDegrees:degrees distanceFromCenter:dist offsetOnly:NO];
}

-(CGPoint) getPointOffsetFromDialAtDegrees:(double) degrees distanceFromCenter:(double) dist offsetOnly:(bool) offsetOnly
{
	degrees = -degrees;
	degrees += 270; //Makes 0 at the top
	degrees += _currentDialRotationInDeg;
	double distanceFromCenter = dist;

	double radians = DEGREES_TO_RADIANS(degrees);
	double offsetX = cos(radians) * distanceFromCenter;
	double offsetY = sin(radians) * distanceFromCenter;

	CGPoint center = _dialCenterPoint;

	if(offsetOnly)
		return CGPointMake((CGFloat) offsetX, (CGFloat) offsetY);
	else
	{
		CGPoint offsetPoint = CGPointMake((CGFloat) (center.x + offsetX), (CGFloat) (center.y + offsetY));
		return offsetPoint;
	}
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
