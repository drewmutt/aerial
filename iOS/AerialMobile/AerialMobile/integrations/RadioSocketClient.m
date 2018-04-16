//
// Created by Andrew Simmons on 6/30/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "RadioSocketClient.h"
#import "RadioConnector.h"


@implementation RadioSocketClient {
    NSInputStream *inputStream;
    NSOutputStream *outputStream;

}
/*
- (void)initNetworkCommunication
{
    NSString *radioURL = [[RadioConnector instance] radioURL];
    NSString *urlStr = [radioURL substringToIndex:radioURL.length - 5];

    if (![urlStr isEqualToString:@""]) {
        NSURL *website = [NSURL URLWithString:urlStr];
        if (!website) {
            NSLog(@"is not a valid URL");
            return;
        }

        CFReadStreamRef readStream;
        CFWriteStreamRef writeStream;
        CFStreamCreatePairWithSocketToHost(NULL, (__bridge CFStringRef) [website host], 8181, &readStream, &writeStream);

        inputStream = (__bridge_transfer NSInputStream *) readStream;
        outputStream = (__bridge_transfer NSOutputStream *) writeStream;
        [inputStream setDelegate:self];
        [outputStream setDelegate:self];
        [inputStream scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
        [outputStream scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
        [inputStream open];
        [outputStream open];

    }
}


- (void)stream:(NSStream *)stream handleEvent:(NSStreamEvent)eventCode {
    NSLog(@"stream:handleEvent: is invoked...");

    switch(eventCode) {
        case NSStreamEventHasSpaceAvailable:
        {
            if (stream == outputStream) {
                NSString * str = [NSString stringWithFormat:@"f\r"];
                const uint8_t * rawstring =
                        (const uint8_t *)[str UTF8String];
                [outputStream write:rawstring maxLength:strlen(rawstring)];
                [outputStream close];
            }
            break;
        }
            // continued ...
    }
}
*/


- (void)initNetworkCommunication
{

    NSString *radioURL = [[RadioConnector instance] radioURL];
    NSString *urlStr = [radioURL substringToIndex:radioURL.length - 5];

    uint portNo = 8181;
    CFReadStreamRef readStream;
    CFWriteStreamRef writeStream;
    CFStreamCreatePairWithSocketToHost(NULL, (CFStringRef)@"10.10.0.84", portNo, &readStream, &writeStream);
    inputStream = (__bridge NSInputStream *)readStream;
    outputStream = (__bridge NSOutputStream *)writeStream;

    [inputStream setDelegate:self];
    [outputStream setDelegate:self];

    [inputStream scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
    [outputStream scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
    [inputStream open];
    [outputStream open];
}


-(void) getFrequency
{
    NSString *response  = @"f\n";
    NSData *data = [[NSData alloc] initWithData:[response dataUsingEncoding:NSASCIIStringEncoding]];
    [outputStream write:[data bytes] maxLength:[data length]];
}
/*
 - (void)stream:(NSStream *)theStream handleEvent:(NSStreamEvent)streamEvent {
 NSLog(@"stream event %i", streamEvent);
 }
 */

- (void)stream:(NSStream *)theStream handleEvent:(NSStreamEvent)streamEvent {
    typedef enum {
        NSStreamEventNone = 0,
        NSStreamEventOpenCompleted = 1 << 0,
        NSStreamEventHasBytesAvailable = 1 << 1,
        NSStreamEventHasSpaceAvailable = 1 << 2,
        NSStreamEventErrorOccurred = 1 << 3,
        NSStreamEventEndEncountered = 1 << 4
    };
    uint8_t buffer[1024];
    int len;

    switch (streamEvent) {

        case NSStreamEventOpenCompleted:
            NSLog(@"Stream opened now");
            break;
        case NSStreamEventHasBytesAvailable:
            NSLog(@"has bytes");
            if (theStream == inputStream) {
                while ([inputStream hasBytesAvailable]) {
                    len = [inputStream read:buffer maxLength:sizeof(buffer)];
                    if (len > 0) {

                        NSString *output = [[NSString alloc] initWithBytes:buffer length:len encoding:NSASCIIStringEncoding];

                        if (nil != output) {
                            NSLog(@"server said: %@", output);
                        }
                    }
                }
            } else {
                NSLog(@"it is NOT theStream == inputStream");
            }
            break;
        case NSStreamEventHasSpaceAvailable:
            NSLog(@"Stream has space available now");
            break;


        case NSStreamEventErrorOccurred:
            NSLog(@"Can not connect to the host!");
            break;


        case NSStreamEventEndEncountered:

            [theStream close];
            [theStream removeFromRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];

            break;

        default:
            NSLog(@"Unknown event %i", streamEvent);
    }

}


@end