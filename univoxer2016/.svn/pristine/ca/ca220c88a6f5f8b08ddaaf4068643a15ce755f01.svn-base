//
//  ServerAPIFacadeTests.m
//  Univoxer
//
//  Created by Marcus Freitas on 4/13/16.
//  Copyright © 2016 Sinapse. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "ServerAPIFacade.h"

@interface ServerAPIFacadeTests : XCTestCase

@end

@implementation ServerAPIFacadeTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testLogin {
    
    XCTestExpectation *expectationLoginWithInvalidUser = [self expectationWithDescription:@"test login with invalid user"];
    
    ServerAPIFacade *api = [[ServerAPIFacade alloc] initWithRequest:[[HttpRequest alloc] init]];
    [api login:@"teste" andPassword:@"teste" andProficiency:@"FR" andCallback:^(NSDictionary *jsonDictionary) {
        
        XCTAssertNotNil(jsonDictionary);
        NSString *code = [jsonDictionary objectForKey:@"code"];
        XCTAssertEqual([code integerValue], 404);
        XCTAssertEqualObjects([jsonDictionary objectForKey:@"message"], @"EMAIL_DOES_NOT_EXIST");
        [expectationLoginWithInvalidUser fulfill];
    }];
    
    XCTestExpectation *expectationWithValidUser = [self expectationWithDescription:@"test login with valid user"];
    [api login:@"dao2n22e@gmail.com" andPassword:@"123123" andProficiency:@"FR" andCallback:^(NSDictionary *jsonDictionary) {
        
        XCTAssertNotNil(jsonDictionary);
        XCTAssertEqual([[jsonDictionary objectForKey:@"code"] integerValue], 200);
        XCTAssertEqualObjects([jsonDictionary objectForKey:@"message"], @"AUTHENTICATED");
        [expectationWithValidUser fulfill];
    }];
    
    [self waitForExpectationsWithTimeout:1.0 handler:nil];
}

- (void)testSaveProfile {
    ServerAPIFacade *api = [[ServerAPIFacade alloc] initWithRequest:[[HttpRequest alloc] init]];
    XCTestExpectation *expectationNewUser = [self expectationWithDescription:@"test new user"];
    XCTestExpectation *expectationEditUser = [self expectationWithDescription:@"test edit user"];
    __block int userId;
    
    [api saveProfileWithUser:@"test@sinapse.com" andUserId:-1 andName:@"Test" andBirthday:@"14-04-2016" andNature:1 andProficiency:@"EN" andRole:1 andPassword:@"123456" andCallback:^(NSDictionary * jsonDictionary) {
        XCTAssertNotNil(jsonDictionary);
        XCTAssertEqualObjects([jsonDictionary objectForKey:@"message"], @"CREATED");
        userId = (int)[jsonDictionary objectForKey:@"id_user"];
        [expectationNewUser fulfill];
        
        [api saveProfileWithUser:@"test@sinapse.com" andUserId:userId andName:@"Test2" andBirthday:@"14-04-2016" andNature:1 andProficiency:@"EN" andRole:1 andPassword:@"123456" andCallback:^(NSDictionary * jsonDictionary) {
            XCTAssertNotNil(jsonDictionary);
            XCTAssertEqualObjects([jsonDictionary objectForKey:@"message"], @"UPDATED");
            //        XCTAssertEqualObjects([jsonDictionary objectForKey:@"name"], @"Test2");
            [expectationEditUser fulfill];
        }];
    }];
    
    [self waitForExpectationsWithTimeout:1.0 handler:nil];
}

- (void)testCallProfile {
    ServerAPIFacade *api = [[ServerAPIFacade alloc] initWithRequest:[[HttpRequest alloc] init]];
    XCTestExpectation *expectation = [self expectationWithDescription:@"test call profile"];
    [api callProfileWithUserId:145 andNature:2 andProficiency:1 andServiceType:3 andCallback:^(NSDictionary *jsonDictionary) {
        XCTAssertNotNil(jsonDictionary);
        XCTAssertEqualObjects([jsonDictionary objectForKey:@"message"], @"CONFERENCE");
        [expectation fulfill];
    }];
    
    [self waitForExpectationsWithTimeout:1.0 handler:nil];
}

- (void)testFinishCall {
    ServerAPIFacade *api = [[ServerAPIFacade alloc] initWithRequest:[[HttpRequest alloc] init]];
    
    __block NSString *callToken;
    XCTestExpectation *callExpectation = [self expectationWithDescription:@"test finish call"];
    XCTestExpectation *finishCallExpectation = [self expectationWithDescription:@"test finish call"];
    [api callProfileWithUserId:145 andNature:2 andProficiency:1 andServiceType:3 andCallback:^(NSDictionary *jsonDictionary) {
        XCTAssertNotNil(jsonDictionary);
        callToken = [jsonDictionary objectForKey:@"call_token"];
        [callExpectation fulfill];
        
        [api finishCallWithToken:callToken andCallback:^(NSDictionary *jsonDictionary) {
            XCTAssertNotNil(jsonDictionary);
            XCTAssertEqualObjects([jsonDictionary objectForKey:@"message"], @"FINISH CONF");
            [finishCallExpectation fulfill];
        }];
    }];
    
    
    
    [self waitForExpectationsWithTimeout:1.0 handler:nil];
}

//- (void)testPerformanceExample {
//    // This is an example of a performance test case.
//    [self measureBlock:^{
//        // Put the code you want to measure the time of here.
//    }];
//}

@end
