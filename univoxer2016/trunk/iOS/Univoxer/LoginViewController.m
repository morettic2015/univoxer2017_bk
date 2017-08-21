//
//  ViewController.m
//  Univoxer
//
//  Created by Marcus Freitas on 4/13/16.
//  Copyright © 2016 Sinapse. All rights reserved.
//

#import "LoginViewController.h"
#import "ServerAPIFacade.h"
#import "HttpRequest.h"
#import "AJWValidator.h"

@interface LoginViewController () {
    ServerAPIFacade *serverAPIFacade;
}

@end

@implementation LoginViewController

@synthesize emailTextField, passwordTextField;

- (void)viewDidLoad {
    [super viewDidLoad];
    serverAPIFacade = [[ServerAPIFacade alloc] initWithRequest:[[HttpRequest alloc] init]];
    self.passwordTextField.delegate = self;
    
    [self setupSigninButtonBorder];
    [self setupTextFieldPlaceHolders];
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    [self setupTextFieldBottomBorders];
}

- (void)setupSigninButtonBorder {
    
    [[self.signinButton layer] setCornerRadius:8.0f];
    [[self.signinButton layer] setMasksToBounds:YES];
    [[self.signinButton layer] setBorderWidth:1.0f];
    [[self.signinButton layer] setBorderColor:[UIColor whiteColor].CGColor];
}

- (void)setupTextFieldPlaceHolders {
    NSAttributedString *emailPlaceHolderString = [[NSAttributedString alloc] initWithString:@"E-mail" attributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    self.emailTextField.attributedPlaceholder = emailPlaceHolderString;
    
    NSAttributedString *passwordPlaceHolderString = [[NSAttributedString alloc] initWithString:@"Password" attributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    self.passwordTextField.attributedPlaceholder = passwordPlaceHolderString;
}

- (void)setupTextFieldBottomBorders {
    CALayer *emailBottomBorder = [CALayer layer];
    CGFloat borderWidth = 1;
    emailBottomBorder.borderColor = [UIColor whiteColor].CGColor;
    emailBottomBorder.frame = CGRectMake(0, self.emailTextField.frame.size.height - borderWidth, self.emailTextField.frame.size.width, self.emailTextField.frame.size.height);
    emailBottomBorder.borderWidth = borderWidth;
    [self.emailTextField.layer addSublayer:emailBottomBorder];
    
    CALayer *passwordBottomBorder = [CALayer layer];
    passwordBottomBorder.borderColor = [UIColor whiteColor].CGColor;
    passwordBottomBorder.frame = CGRectMake(0, self.passwordTextField.frame.size.height - borderWidth, self.passwordTextField.frame.size.width, self.passwordTextField.frame.size.height);
    passwordBottomBorder.borderWidth = borderWidth;
    [self.passwordTextField.layer addSublayer:passwordBottomBorder];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    [self signinOrRegisterButtonClick:textField];
    return YES;
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [[self view] endEditing:YES];
}

- (void) showMessage:(NSString *)message andOnOkAction:(void (^)(UIAlertAction * _Nonnull))handler {
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Login" message:message preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *okButton = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:handler];
    
    [alertController addAction:okButton];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

- (BOOL)validateFields {
    AJWValidator *emailValidator = [AJWValidator validatorWithType:AJWValidatorTypeString];
    [emailValidator addValidationToEnsureValidEmailWithInvalidMessage:NSLocalizedString(@"That's not an email", nil)];
    [emailValidator addValidationToEnsurePresenceWithInvalidMessage:NSLocalizedString(@"The e-mail field is empty", nil)];
    [emailValidator validate:emailTextField.text];
    if (![emailValidator isValid]) {
        [self showMessage:[[emailValidator errorMessages] objectAtIndex:0] andOnOkAction:^(UIAlertAction * _Nonnull action) {
            [self.emailTextField becomeFirstResponder];
        }];
        return NO;
    }
    
    AJWValidator *passwordValidator = [AJWValidator validatorWithType:AJWValidatorTypeString];
    [passwordValidator addValidationToEnsurePresenceWithInvalidMessage:NSLocalizedString(@"The password field is empty", nil)];
    [passwordValidator validate:passwordTextField.text];
    if (![passwordValidator isValid]) {
        [self showMessage:[[passwordValidator errorMessages] objectAtIndex:0] andOnOkAction:^(UIAlertAction * _Nonnull action) {
            [self.passwordTextField becomeFirstResponder];
        }];
        return NO;
    }
    
    return YES;
}

- (IBAction)signinOrRegisterButtonClick:(id)sender {
    if ([self validateFields]) {
        if (serverAPIFacade) {
            [serverAPIFacade login:emailTextField.text andPassword:passwordTextField.text andProficiency:@"pt-br" andCallback:^(NSDictionary *jsonDictionary) {
                if (jsonDictionary) {
                    if ([[jsonDictionary objectForKey:@"code"] integerValue] == 404) {
                        if ([[jsonDictionary objectForKey:@"message"] isEqualToString:@"EMAIL_DOES_NOT_EXIST"]) {
                            dispatch_async(dispatch_get_main_queue(), ^{
                                [self performSegueWithIdentifier:@"registerUserSegue" sender:self];
                            });
                        } else {
                            [self showMessage:NSLocalizedString(@"Invalid Password", nil) andOnOkAction:^(UIAlertAction * _Nonnull action) {
                                [self.passwordTextField becomeFirstResponder];
                            }];
                        }
                    } else {
                        dispatch_async(dispatch_get_main_queue(), ^{
                            [self performSegueWithIdentifier:@"homeSegue" sender:self];
                        });
                    }
                }
            }];
        }
    }
}


@end
