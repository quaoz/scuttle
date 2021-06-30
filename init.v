import net.http
import json { decode }
import os { input }

struct License {
	key     string [skip]
	name    string
	spdx_id string
	url     string
	node_id string [skip]
}

struct License_Text {
	key            string   [skip]
	name           string   [skip]
	spdx_id        string   [skip]
	url            string   [skip]
	node_id        string   [skip]
	html_url       string   [skip]
	description    string   [skip]
	implementation string   [skip]
	permissions    []string [skip]
	conditions     []string [skip]
	limitations    []string [skip]
	body           string
	featured       bool     [skip]
}

struct Not_Found {
	message string
	documentation_url string [skip]
}

fn get(url string) ?string {
	mut request := http.Request{
		method: http.Method.get
		url: url
	}

	request.add_header(http.CommonHeader.accept, 'application/vnd.github.v3+json')
	mut resp := request.do() or { panic(err) }

	return resp.text
}

fn pick_license() {
	url_base := 'https://api.github.com/licenses'
	mut url := url_base
	mut data := get(url) or { panic(err.msg) }
	licenses := json.decode([]License, data) or { panic('Failed to decode json') }

	println('Pick a license (by entering a number):\n')
	for i, license in licenses {
		println('	${i + 1}. $license.name ($license.spdx_id)')
	}

	num_licenses := licenses.len
	println('	${num_licenses + 1}. A different license\n')

	mut invalid_key := true
	mut license := licenses[0]
	mut selection := input('').int()
	mut license_not_found := Not_Found {
		message : ''
	}
	mut processed_license := License_Text {
		body : ''
	}

	for true {
		if selection > 0 && selection <= num_licenses {
			license = licenses[selection - 1]
			license_text := create_license(license)
			to_file(license_text)
			return
		} else if selection == num_licenses + 1 {
			for true {
				key := input('Enter the key for the license, e.g. lgpl-3.0\n')
				url = url_base + '/' + key
				invalid_key = false
				data = get(url) or { panic('Failed to retrive data') }

				processed_license = json.decode(License_Text, data) or {
					invalid_key = true
					println('Failed to decode json')
					break
				}

				license_not_found = json.decode(Not_Found, data) or { continue }
				if license_not_found.message == 'Not Found' {
					println('Invalid key')
					invalid_key = true
				}

				if !invalid_key {
					to_file(processed_license)
					return
				}
			}
		} else {
			selection = input('').int()
		}
	}
}

fn create_license(license License) License_Text {
	url := license.url
	data := get(url) or { panic(err.msg) }

	body := json.decode(License_Text, data) or { panic('Failed to decode json') }
	return body
}

fn to_file(license License_Text) {
	mut file := os.create('LICENSE') or { panic(err) }
	file.write_string(license.body) or { panic(err) }
	file.close()
}

fn main() {
	pick_license()
}
