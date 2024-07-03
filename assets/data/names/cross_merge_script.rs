use itertools::Itertools;

type PathLike = dyn core::ops::Deref<Target=std::path::Path>;
pub fn cross_merge(in_1: &PathLike, in_2: &PathLike, out: &PathLike){
    use std::{
        fs,
    };
    let text_1 = fs::read_to_string(&**in_1).unwrap();
    let lines_1= text_1.split('\n')
        .map(|line| line.trim());
    let text_2 = fs::read_to_string(&**in_2).unwrap();
    let lines_2= text_2.split('\n')
        .map(|line| line.trim());
    let mut lines_cross_prod = itertools::iproduct!(lines_1, lines_2)
        .collect_vec();

    // Shuffle
    use rand::seq::SliceRandom;
    lines_cross_prod.shuffle(&mut rand::thread_rng());

    let mut out_buf = String::new();
    for (lhs_line, rhs_line) in lines_cross_prod{
        use std::fmt::Write;
        writeln!(out_buf, "{lhs_line} {rhs_line}").unwrap();
    }

    fs::write(&**out, &out_buf).unwrap();
}

fn main() {
    use std::path::Path;

    let dir: &Path= r"C:\Users\HP\Desktop\GUI_1\assets\data\names\".as_ref();
    let first_names= dir.join("first_names.txt");
    let last_names= dir.join("last_names.txt");
    let senders= dir.join("senders.txt");

    cross_merge(&first_names, &last_names, &senders);
}
