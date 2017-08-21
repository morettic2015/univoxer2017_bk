//
//  RegisterUserViewController.m
//  Univoxer
//
//  Created by Marcus Freitas on 5/30/16.
//  Copyright © 2016 Sinapse. All rights reserved.
//

#import "RegisterUserViewController.h"
#import "ServerAPIFacade.h"
#import "HomeViewController.h"
#import "AJWValidator.h"

@interface RegisterUserViewController () {
    ServerAPIFacade *serverAPIFacade;
    
}
@end

@implementation RegisterUserViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    serverAPIFacade = [[ServerAPIFacade alloc] initWithRequest:[[HttpRequest alloc] init]];
    [self setupTextFieldPlaceHolders];
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    [self setupTextFieldBottomBorders];
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [[self view] endEditing:YES];
}

- (IBAction)cancel:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void) showMessage:(NSString *)message andOnOkAction:(void (^)(UIAlertAction * _Nonnull))handler {
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Register" message:message preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *okButton = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:handler];
    
    [alertController addAction:okButton];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

- (void)setupTextFieldPlaceHolders {
    NSAttributedString *namePlaceHolderString = [[NSAttributedString alloc] initWithString:@"Name" attributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    self.nameTextField.attributedPlaceholder = namePlaceHolderString;
    
    NSAttributedString *emailPlaceHolderString = [[NSAttributedString alloc] initWithString:@"E-mail" attributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    self.emailTextField.attributedPlaceholder = emailPlaceHolderString;
    
    NSAttributedString *passwordPlaceHolderString = [[NSAttributedString alloc] initWithString:@"Password" attributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    self.passwordTextField.attributedPlaceholder = passwordPlaceHolderString;
    
    NSAttributedString *confirmPasswordPlaceHolderString = [[NSAttributedString alloc] initWithString:@"Confirm Password" attributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    self.confirmPasswordTextField.attributedPlaceholder = confirmPasswordPlaceHolderString;
}

- (CALayer *)createBottomBorder {
    CALayer *border = [CALayer layer];
    CGFloat borderWidth = 1;
    border.borderColor = [UIColor whiteColor].CGColor;
    border.frame = CGRectMake(0, self.emailTextField.frame.size.height - borderWidth, self.emailTextField.frame.size.width, self.emailTextField.frame.size.height);
    border.borderWidth = borderWidth;
    
    return border;
}

- (void)setupTextFieldBottomBorders {
    [self.nameTextField.layer addSublayer:[self createBottomBorder]];
    [self.emailTextField.layer addSublayer:[self createBottomBorder]];
    [self.passwordTextField.layer addSublayer:[self createBottomBorder]];
    [self.confirmPasswordTextField.layer addSublayer:[self createBottomBorder]];
}

- (BOOL)validateFields {
    AJWValidator *nameValidator = [AJWValidator validatorWithType:AJWValidatorTypeString];
    [nameValidator addValidationToEnsurePresenceWithInvalidMessage:NSLocalizedString(@"The name field is empty", nil)];
    [nameValidator validate:self.nameTextField.text];
    if (![nameValidator isValid]) {
        [self showMessage:[[nameValidator errorMessages] objectAtIndex:0] andOnOkAction:^(UIAlertAction * _Nonnull action) {
            [self.nameTextField becomeFirstResponder];
        }];
        return NO;
    }
    
    
    AJWValidator *emailValidator = [AJWValidator validatorWithType:AJWValidatorTypeString];
    [emailValidator addValidationToEnsureValidEmailWithInvalidMessage:NSLocalizedString(@"That's not an email", nil)];
    [emailValidator addValidationToEnsurePresenceWithInvalidMessage:NSLocalizedString(@"The e-mail field is empty", nil)];
    [emailValidator validate:self.emailTextField.text];
    if (![emailValidator isValid]) {
        [self showMessage:[[emailValidator errorMessages] objectAtIndex:0] andOnOkAction:^(UIAlertAction * _Nonnull action) {
            [self.emailTextField becomeFirstResponder];
        }];
        return NO;
    }
    
    AJWValidator *passwordValidator = [AJWValidator validatorWithType:AJWValidatorTypeString];
    [passwordValidator addValidationToEnsurePresenceWithInvalidMessage:NSLocalizedString(@"The password field is empty", nil)];
    [passwordValidator validate:self.passwordTextField.text];
    if (![passwordValidator isValid]) {
        [self showMessage:[[passwordValidator errorMessages] objectAtIndex:0] andOnOkAction:^(UIAlertAction * _Nonnull action) {
            [self.passwordTextField becomeFirstResponder];
        }];
        return NO;
    }
    
    AJWValidator *confirmPasswordValidator = [AJWValidator validatorWithType:AJWValidatorTypeString];
    [confirmPasswordValidator addValidationToEnsurePresenceWithInvalidMessage:NSLocalizedString(@"The confirm password field is empty", nil)];
    [confirmPasswordValidator addValidationToEnsureInstanceIsTheSameAs:self.passwordTextField.text invalidMessage:NSLocalizedString(@"The confirm password doesn't match", nil)];
    [confirmPasswordValidator validate:self.confirmPasswordTextField.text];
    if (![confirmPasswordValidator isValid]) {
        [self showMessage:[[confirmPasswordValidator errorMessages] objectAtIndex:0] andOnOkAction:^(UIAlertAction * _Nonnull action) {
            [self.confirmPasswordTextField becomeFirstResponder];
        }];
        return NO;
    }
    
    return YES;
}

- (IBAction)saveOrNext:(id)sender {
    [serverAPIFacade saveProfileWithUser:self.emailTextField.text andUserId:-1 andName:self.nameTextField.text andBirthday:nil andNature:1 andProficiency:@"PT" andRole:0 andPassword:self.passwordTextField.text andCallback:^(NSDictionary *jsonDictionary) {
        if ([[jsonDictionary objectForKey:@"message"] isEqualToString:@"CREATED"]) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [self showMessage:@"User saved with success!" andOnOkAction:nil];
            });
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                [self showMessage:@"There was an error while saving!" andOnOkAction:nil];
            });
        }
    }];
}

@end
