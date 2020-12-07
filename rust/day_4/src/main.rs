use std::collections::HashMap;
use itertools::{Itertools};

use std::str::FromStr;
use std::{fs};
use regex::Regex;

#[derive(PartialEq, Debug)]
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
        let required_fields = vec!["byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"];
        for field in required_fields {
            if !map.contains_key(field) {
                return Err(String::from("Necessary fields were not found"));
            }
        }
        if validate {
            let byr = *map.get("byr").unwrap_or(&"not given");
            if is_in_range(byr, 1920, 2002).is_err() {
                return Err(String::from("Birth year for valid!"));
            }
            let iyr = *map.get("iyr").unwrap_or(&"not given");
            if is_in_range(iyr, 2010, 2020).is_err() {
                return Err(String::from("Issue year for valid!"));
            }
            let eyr = *map.get("eyr").unwrap_or(&"not given");
            if is_in_range(eyr, 2020, 2030).is_err() {
                return Err(String::from("Issue year for valid!"));
            }
            let hgt = *map.get("hgt").unwrap_or(&"not given");
            if parse_height(hgt).is_err() {
                return Err(String::from("Issue year for valid!"));
            }
            let hcl = *map.get("hcl").unwrap_or(&"not given");
            if parse_hair_colour(hcl).is_err() {
                return Err(String::from("Issue year for valid!"));
            }
            let ecl = *map.get("ecl").unwrap_or(&"not given");
            if parse_eye_colour(ecl).is_err() {
                return Err(String::from("Issue year for valid!"));
            }
            let pid = *map.get("pid").unwrap_or(&"not given");
            if parse_passport_id(pid).is_err() {
                return Err(String::from("Issue year for valid!"));
            }
        }
        let passport = Passport {
            byr: String::from(*map.get("byr").unwrap_or(&"not given")),
            iyr: String::from(*map.get("iyr").unwrap_or(&"not given")),
            eyr: String::from(*map.get("eyr").unwrap_or(&"not given")),
            hgt: String::from(*map.get("hgt").unwrap_or(&"not given")),
            hcl: String::from(*map.get("hcl").unwrap_or(&"not given")),
            ecl: String::from(*map.get("ecl").unwrap_or(&"not given")),
            pid: String::from(*map.get("pid").unwrap_or(&"not given")),
            cid: String::from(*map.get("cid").unwrap_or(&"not given")),
        };
        // println!("Match found for {:?}", passport);
        Ok(passport)
    }

}

#[derive(PartialEq, Debug)]
enum ValidationError {
    OutOfBounds,
    FormatError,
    ParseValueError
}

fn validate_years(value: u16, low: u16, high: u16) -> Result<bool, ValidationError> {
    return if value < low || value > high {
        Err(ValidationError::OutOfBounds)
    } else {
        Ok(true)
    }
}

fn parse_hair_colour(s: &str) -> Result<bool, ValidationError> {
    let re = Regex::new(r"^#[0-9a-f]{6}$").unwrap();
    match re.is_match(s) {
        true => Ok(true),
        false => Err(ValidationError::FormatError)
    }
}

fn parse_passport_id(s: &str) -> Result<bool, ValidationError> {
    let re = Regex::new(r"^[0-9]{9}$").unwrap();
    match re.is_match(s) {
        true => Ok(true),
        false => Err(ValidationError::FormatError)
    }
}

fn parse_eye_colour(color: &str) -> Result<bool, ValidationError> {
    match color {
        "amb" | "blu" | "brn" | "gry" | "grn" | "hzl" | "oth" => Ok(true),
        _ => Err(ValidationError::FormatError)
    }
}

fn parse_height(s: &str) -> Result<bool, ValidationError> {
    let re = Regex::new(r"^(\d{2,3})(cm|in)$").unwrap();
    let matches: Vec<_> = re.captures_iter(s).collect();
    if matches.len() == 0 {
        // println!("No matches for {}", s);
        return Err(ValidationError::FormatError);
    }
    // println!("{}-{}-{}", &matches[0][0], &matches[0][1], &matches[0][2]);
    if &matches[0][2] == "cm" {
        is_in_range(&matches[0][1], 150, 193)
    } else if &matches[0][2] == "in" {
        is_in_range(&matches[0][1], 59, 76)
    } else {
        Err(ValidationError::FormatError)
    }
}

fn is_in_range(s: &str, low: u16, high: u16) -> Result<bool, ValidationError> {
    match s.parse::<u16>() {
        Err(_) => Err(ValidationError::ParseValueError),
        Ok(year) => {
            validate_years(year, low, high)
        }
    }
}

fn readfile(filename: &str) -> String {
    fs::read_to_string(filename)
        .expect("Something went wrong reading the file")
}

fn main() {
    let contents = readfile("input.txt");
    let passports_to_process = contents.split("\n\n");
    let num_all_passports = passports_to_process.clone().count();
    let valid_passports = passports_to_process
        .map(|x| x.trim().parse::<Passport>())
        .filter(|x| x.is_ok());

    println!("Number of all passports is {}", num_all_passports);
    println!("Number of valid passports is {}", valid_passports.count());
}


#[cfg(test)]
mod tests {
    use crate::{Passport, is_in_range, parse_height};

    #[test]
    fn test_parse_height() {
        assert!(parse_height("150cm").is_ok())
    }

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
        assert!(is_in_range("1920", 1920, 2002).is_ok());
        assert!(is_in_range("2002", 1920, 2002).is_ok());
        assert!(is_in_range("1000", 1920, 2002).is_err());
        assert!(is_in_range("3000", 1920, 2002).is_err());
    }
}