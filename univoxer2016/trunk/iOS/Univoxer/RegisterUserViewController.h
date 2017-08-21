//
//  RegisterUserViewController.h
//  Univoxer
//
//  Created by Marcus Freitas on 5/30/16.
//  Copyright © 2016 Sinapse. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RegisterUserViewController : UITableViewController

@property (weak, nonatomic) IBOutlet UITextField *emailTextField;
@property (weak, nonatomic) IBOutlet UITextField *passwordTextField;
@property (weak, nonatomic) IBOutlet UITextField *confirmPasswordTextField;
@property (weak, nonatomic) IBOutlet UITextField *nameTextField;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *saveBarButtonItem;
@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;

@end
