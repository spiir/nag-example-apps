//
//  ViewController.swift
//  QuickSprout
//
//  Created by Brian Vestergaard Danielsen on 04/10/2018.
//  Copyright © 2018 Spiir A/S. All rights reserved.
//

import UIKit

protocol HomeViewControllerDelegate: AnyObject {
    func didReceiveCode(code: String)
}

class HomeViewController: UIViewController {
    
    @IBOutlet weak var paymentButton: UIButton!
    @IBOutlet weak var webViewButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        styleButton(button: paymentButton)
        styleButton(button: webViewButton)
    }
    
    func styleButton(button: UIButton) -> Void {
        button.layer.cornerRadius = 20
        button.clipsToBounds = true
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "webViewSegue" {
            (segue.destination as! WebViewViewController).delegate = self
        }
    }
}

extension HomeViewController: HomeViewControllerDelegate {
    func didReceiveCode(code: String) {
        QSAPI.tokens(code: code) {
            (accessToken) in
            QSAPI.accounts(accessToken: accessToken) {
                (accountsResponse) in
                let viewController = UIStoryboard.instantiateAccountsViewController()
                viewController.accounts = accountsResponse.accounts
                viewController.accessToken = accessToken
                self.navigationController?.pushViewController(viewController, animated: true)
            }
        }
    }
}
