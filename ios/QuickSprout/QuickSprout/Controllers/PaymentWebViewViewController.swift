//
//  PaymentWebViewViewController.swift
//  QuickSprout
//
//  Created by Sylwia Mroczkowska on 15/01/2020.
//  Copyright © 2020 Brian Vestergaard Danielsen. All rights reserved.
//

import UIKit
import WebKit
import NAGLoginSDK

class PaymentWebViewViewController: UIViewController {
    
    let notificationName = "urlSchemeTriggered"
    let urlScheme = "nagdemoapp"
    weak var delegate: HomeViewControllerDelegate? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let view = self.view as? NAGLoginView {
            QSAPI.createPayment(language: "dk", redirectUrl: "nagdemoapp://") {
                (url) in
                NotificationCenter.default.addObserver(self, selector: #selector(self.urlSchemeTriggered(_:)), name: Notification.Name(self.notificationName), object: nil)
                let url = URL(string:url)
                let request = URLRequest(url: url!)
                view.notificationName = self.notificationName
                view.urlScheme = self.urlScheme
                view.appWebView.load(request)
            }
        }
    }
    
    @objc func urlSchemeTriggered(_ notification : Notification) {
        guard let url = notification.object as? URL else {
            return
        }
        NotificationCenter.default.removeObserver(self, name: Notification.Name(self.notificationName), object: nil)
        let paymentId = url.getQueryString(parameter: "paymentId")
        print("PaymentId: " + paymentId!)
        self.dismiss(animated: true, completion: nil)
    }
}
