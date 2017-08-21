//
//  HttpRequest.m
//  Univoxer
//
//  Created by Marcus Freitas on 4/13/16.
//  Copyright © 2016 Sinapse. All rights reserved.
//

#import "HttpRequest.h"

NSString *const kBaseUrl = @"http://morettic.com.br/babel_json_services/?";

@implementation HttpRequest

- (void) requestWithMethod:(NSString *)method andParams:(NSString *) params andCallback:(void (^)(NSDictionary *)) callback {
    
//    NSString *encodedParams = [params stringByAddingPercentEncodingWithAllowedCharacters:[NSMutableCharacterSet alphanumericCharacterSet]];
    NSString *requestStringURL = [NSString stringWithFormat:@"%@%@%@", kBaseUrl, method, params];
    NSURL *requestURL = [NSURL URLWithString:requestStringURL];
    NSURLSession *session = [NSURLSession sharedSession];
    
    NSURLSessionTask *requestTask = [session dataTaskWithURL:requestURL completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        if (data != nil) {
            NSError *parseError = nil;
            NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:data options:0 error:&parseError];
            NSLog(@"json returned: %@", jsonDictionary);
            if (!parseError) {
                callback(jsonDictionary);
            } else {
                NSString *err = [parseError localizedDescription];
                NSLog(@"Encountered error while parsing: %@", err);
            }
        }
        
    }];
    
    [requestTask resume];
}

@end
