//
//  HttpRequest.h
//  Univoxer
//
//  Created by Marcus Freitas on 4/13/16.
//  Copyright © 2016 Sinapse. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HttpRequest : NSObject

- (void) requestWithMethod:(NSString *)method andParams:(NSString *) params andCallback:(void (^)(NSDictionary*)) callback;

@end
