//
//  QSAPI.swift
//  QuickSprout
//
//  Created by Brian Vestergaard Danielsen on 08/10/2018.
//  Copyright © 2018 Spiir A/S. All rights reserved.
//

import Foundation


public struct QSAPI {
    
    static let BASE_URL: String = "http://localhost:3000"
    
    static func initAuth(endpoint: URL, language: String, redirectUrl: String, completionBlock: @escaping (String) -> Void) {
        let parameters = ["language" : language, "redirectUrl": redirectUrl,]
        if let request = buildPostRequest(endpoint: endpoint, body: parameters) {
            let session = URLSession.shared
            session.dataTask(with: request) { (data, response, error) in
                if let data = data {
                    do {
                        let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
                        
                        if let authUrl = json?["authUrl"] as? String {
                            DispatchQueue.main.async {
                                completionBlock(authUrl);
                            }
                        }
                        
                        if let error = json?["error"] as? String {
                            print(error)
                        }
                    } catch {
                        print(error)
                    }
                }
                }.resume()
        }
    }
    
    static func createPayment(language: String, redirectUrl: String, completionBlock: @escaping (String) -> Void) {
        let parameters = ["language" : language, "redirectUrl": redirectUrl,]
        if let request = buildPostRequest( endpoint: URL(string: BASE_URL + "/create-payment")!, body: parameters) {
            let session = URLSession.shared
            session.dataTask(with: request) { (data, response, error) in
                if let data = data {
                    do {
                        let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
                        
                        if let authUrl = json?["redirectUrl"] as? String {
                            DispatchQueue.main.async {
                                completionBlock(authUrl);
                            }
                        }
                        
                        if let error = json?["error"] as? String {
                            print(error)
                        }
                    } catch {
                        print(error)
                    }
                }
                }.resume()
        }
    }
    
    static func tokens(code: String, completionBlock: @escaping (String) -> Void) {
        let params = ["code" : code]
        if let request = buildPostRequest(endpoint: URL(string: BASE_URL + "/tokens")!, body: params) {
            let session = URLSession.shared
            session.dataTask(with: request) { (data, response, error) in
                if let data = data {
                    do {
                        let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
                        
                        if let session = json?["session"] as? [String: Any], let accessToken = session["accessToken"] as? String {
                            DispatchQueue.main.async {
                                completionBlock(accessToken);
                            }
                        }
                    } catch {
                        print(error)
                    }
                }
                }.resume()
        }
    }
    
    static func accounts(accessToken: String, completionBlock: @escaping (QSGetAccountsResponse) -> Void) {
        let params = ["token" : accessToken]
        if let request = buildPostRequest(endpoint: URL(string: BASE_URL + "/accounts")!, body: params) {
            let session = URLSession.shared
            session.dataTask(with: request) { (data, response, error) in
                if let data = data {
                    do {
                        let jsonString = String(decoding: data, as: UTF8.self)
                        let jsonData = jsonString.data(using: .utf8)
                        
                        if let jsonData = jsonData {
                            let decoder = JSONDecoder()
                            
                            do {
                                let accountsResponse = try decoder.decode(QSGetAccountsResponse.self, from: jsonData)
                                
                                DispatchQueue.main.async {
                                    completionBlock(accountsResponse);
                                }
                            } catch {
                                print(error.localizedDescription)
                            }
                        }
                    }
                }
                }.resume()
        }
    }
    
    static func transactions(accessToken: String, accountId: String, pagingToken: String?, completionBlock: @escaping (QSGetTransactionsResponse) -> Void) {
        let params = ["token" : accessToken]
        
        var url = BASE_URL + "/accounts/transactions?id=\(accountId)"
        if let actualPagingToken = pagingToken {
            url += "&pagingToken=" + actualPagingToken
        }
        
        if let request = buildPostRequest(endpoint: URLUtils.url(url: url), body: params) {
            let session = URLSession.shared
            session.dataTask(with: request) { (data, response, error) in
                if let data = data {
                    do {
                        let jsonString = String(decoding: data, as: UTF8.self)
                        let jsonData = jsonString.data(using: .utf8)
                        
                        if let jsonData = jsonData {
                            let decoder = JSONDecoder()
                            
                            do {
                                let transactionsResponse = try decoder.decode(QSGetTransactionsResponse.self, from: jsonData)
                                
                                DispatchQueue.main.async {
                                    completionBlock(transactionsResponse);
                                }
                            } catch {
                                print(error.localizedDescription)
                            }
                        }
                    }
                }
            }.resume()
        }
    }
    
    
    private static func buildPostRequest(endpoint: URL?, body: [String: String]) -> URLRequest? {
        guard let endpoint = endpoint else {
            print("Invalid URL")
            return nil
        }
        var request = URLRequest(url: endpoint)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        guard let httpBody = try? JSONSerialization.data(withJSONObject: body, options: []) else {
            print("Invalid JSON body")
            return nil
        }
        request.httpBody = httpBody
        return request
    }
}
