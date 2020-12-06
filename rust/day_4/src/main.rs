use std::collections::HashMap;
use itertools::{Itertools};

use std::str::FromStr;
use std::{fs};

struct Passport {
    byr: String,
    iyr: String,
    eyr: String,
    hgt: String,
    hcl: String,
    ecl: String,
    pid: String,
    cid: String
}

impl FromStr for Passport {
    type Err = String;
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let validate = true;
        let input_no_newlines = s.trim().replace("\n", " ");
        let map: HashMap<&str, &str> = input_no_newlines
            .split(" ")
            .flat_map(|item| item.split(":"))
            .tuples()
            .collect();
        // println!("{:?}", map);
        let required_fields = vec!["byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"];
        for field in required_fields {
            if !map.contains_key(field) {
                return Err(String::from("Necessary fields were not found"));
            }
        }
        // if validate {
        //     let byr = *map.get("byr").unwrap_or(&"not given");
        //     if validate_byr(byr).is_err() {
        //         return Err(String::from("Birth year for valid!"));
        //     }
        // }
        Ok(Passport {
            byr: String::from(*map.get("byr").unwrap_or(&"not given")),
            iyr: String::from(*map.get("iyr").unwrap_or(&"not given")),
            eyr: String::from(*map.get("eyr").unwrap_or(&"not given")),
            hgt: String::from(*map.get("hgt").unwrap_or(&"not given")),
            hcl: String::from(*map.get("hcl").unwrap_or(&"not given")),
            ecl: String::from(*map.get("ecl").unwrap_or(&"not given")),
            pid: String::from(*map.get("pid").unwrap_or(&"not given")),
            cid: String::from(*map.get("cid").unwrap_or(&"not given")),
        })
    }

}

// fn validate_byr(s: &str) -> Result<bool, Box<dyn error::Error>> {
//     let year = s.parse::<u16>()?;
//     if year >= 1920 && year <= 2002 {
//         return Ok(true);
//     }
//     Err(error::Error("Failure"))
// }

fn readfile(filename: &str) -> String {
    fs::read_to_string(filename)
        .expect("Something went wrong reading the file")
}

fn main() {
    let contents = readfile("input.txt");
    let passports_to_process = contents.split("\n\n");
    let num_all_passports = passports_to_process.clone().count();
    let valid_passports = passports_to_process
        .map(|x| x.parse::<Passport>())
        .filter(|x| x.is_ok());

    println!("Number of all passports is {}", num_all_passports);
    println!("Number of valid passports is {}", valid_passports.count());
}


#[cfg(test)]
mod tests {
    use crate::{Passport, validate_byr};

    #[test]
    fn it_works() {
        let test_data = String::from(
            "iyr:2018 hgt:164cm hcl:#650d28 byr:1973 cid:108 pid:#b0df80 ecl:blu eyr:2020");
        let passport = test_data.parse::<Passport>();
        assert!(passport.is_ok());
    }
    #[test]
    fn it_doesnt_work() {
        let test_data = String::from(
            "iyr:2018 hgt:164cm hcl:#650d28 byr:1973 cid:108 pid:#b0df80 ecl:blu");
        let passport = test_data.parse::<Passport>();
        assert!(passport.is_err());
    }

    #[test]
    fn can_parse_year() {
        let is_valid = validate_byr("1920");
        assert!(is_valid.is_ok());
    }
}