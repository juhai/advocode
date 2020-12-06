use std::fs;
use itertools::Itertools;

fn get_result_star_1(values: &Vec<u16>) -> u32 {
    let mut s_values = values.clone();
    s_values.sort();
    let mut b = 0;
    let mut e = s_values.len()-1;
    while b < e {
        let sum = s_values[b] + s_values[e];
        if sum == 2020 {
            break;
        } else if sum > 2020 {
            e -= 1;
        } else {
            b += 1;
        }
        if b >= e {return 0;}
    }
    s_values[e] as u32 * s_values[b] as u32
}


fn get_result_star_2(values: &Vec<u16>) -> u32 {
    let mut s_values = values.clone();
    s_values.sort();
    let pairs = s_values
        .iter()
        .cartesian_product(&s_values)
        .filter(|x| x.0 + x.1 <= 2020)
        .cartesian_product(&s_values)
        .map(|x| (x.0.0, x.0.1, x.1))
        .filter(|x| x.0 + x.1 + x.2 == 2020)
        .collect::<Vec<(&u16, &u16, &u16)>>();
    *pairs[0].0 as u32 * *pairs[0].1 as u32 * *pairs[0].2 as u32
}

fn readfile(filename: &str) -> Vec<u16> {
    let contents = fs::read_to_string(filename)
        .expect("Something went wrong reading the file");
    let lines: Vec<u16> = contents
        .split("\n")
        .filter(|x| x.len() > 0)
        .map(|x| x.parse::<u16>().unwrap())
        .collect::<Vec<u16>>();
    lines
}

fn main() {
    let filename = "../../python/day1_data.txt";
    let values = readfile(filename);

    let result_star_1 = get_result_star_1(&values);
    println!("STAR 1: {}", result_star_1);

    let result_star_2 = get_result_star_2(&values);
    println!("STAR 2: {}", result_star_2);
}
