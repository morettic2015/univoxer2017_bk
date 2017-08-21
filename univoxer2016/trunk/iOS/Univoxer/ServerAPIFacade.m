//
//  ServerAPIFacade.m
//  Univoxer
//
//  Created by Marcus Freitas on 4/13/16.
//  Copyright © 2016 Sinapse. All rights reserved.
//

#import "ServerAPIFacade.h"
#import "HttpRequest.h"

NSString *const kLoginToken = @"login=";
NSString *const kActionSaveProfileToken = @"action=SAVE_PROFILE";
NSString *const kActionCallProfileToken = @"action=CALL_PROFILE";
NSString *const kActionFinishCallToken = @"action=FINISH_PROFILE";

@implementation ServerAPIFacade

@synthesize request;

- (id) initWithRequest:(HttpRequest *) newRequest {
    self = [super init];
    if (self != nil) {
        self.request = newRequest;
    }
    
    return self;
}

- (void) login:(NSString *)user andPassword:(NSString *)password andProficiency:(NSString *)proficiency andCallback:(void (^)(NSDictionary *)) callback {
    [self.request requestWithMethod:kLoginToken andParams:[NSString stringWithFormat:@"%@&passwd=%@&proficiency=%@", user, password, proficiency] andCallback:callback];
}

- (void) saveProfileWithUser:(NSString *)user andUserId:(int)userId andName:(NSString *)name andBirthday:(NSString *)birthday andNature:(int)nature
      andProficiency:(NSString *)proficiency andRole:(int)role andPassword:(NSString *)password andCallback:(void (^)(NSDictionary *)) callback {
    NSString *params = [NSString stringWithFormat:@"&id_user=%d&email=%@&name=%@&birthday=%@&paypall_acc=-1&nature=%d&proficiency=%@&id_role=%d&passwd=%@",
                        userId, user, name, birthday, nature, proficiency, role, password];
    [self.request requestWithMethod:kActionSaveProfileToken andParams:params andCallback:callback];
}

- (void) callProfileWithUserId:(int)userId andNature:(int)nature andProficiency:(int)proficiency andServiceType:(int)serviceType andCallback:(void (^)(NSDictionary *)) callback {
    NSString *params = [NSString stringWithFormat:@"&nature=%d&proficiency=%d&id_user=%d&id_service_type=%d", nature, proficiency, userId, serviceType];
    [self.request requestWithMethod:kActionCallProfileToken andParams:params andCallback:callback];
}

- (void) finishCallWithToken:(NSString *)token andCallback:(void (^)(NSDictionary *)) callback {
    NSString *params = [NSString stringWithFormat:@"&token=%@", token];
    [self.request requestWithMethod:kActionFinishCallToken andParams:params andCallback:callback];
}

@end
