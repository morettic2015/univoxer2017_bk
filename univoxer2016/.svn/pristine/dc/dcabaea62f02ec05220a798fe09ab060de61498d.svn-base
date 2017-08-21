//
//  ServerAPIFacade.h
//  Univoxer
//
//  Created by Marcus Freitas on 4/13/16.
//  Copyright © 2016 Sinapse. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HttpRequest.h"

@interface ServerAPIFacade : NSObject

- (id) initWithRequest:(HttpRequest *) newRequest;

- (void) login: (NSString *)user andPassword:(NSString *) password andProficiency:(NSString *) proficiency andCallback:(void (^)(NSDictionary *)) callback;
- (void) saveProfileWithUser:(NSString *)user andUserId:(int)userId andName:(NSString *)name andBirthday:(NSString *)birthday andNature:(int)nature
      andProficiency:(NSString *)proficiency andRole:(int)role andPassword:(NSString *)password andCallback:(void (^)(NSDictionary *)) callback;
- (void) callProfileWithUserId:(int)userId andNature:(int)nature andProficiency:(int)proficiency andServiceType:(int)serviceType andCallback:(void (^)(NSDictionary *)) callback;
- (void) finishCallWithToken:(NSString *)token andCallback:(void (^)(NSDictionary *)) callback;


@property (nonatomic, retain) HttpRequest *request;

@end
