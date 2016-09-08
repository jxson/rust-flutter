extern crate jni_sys;

use std::os::raw::c_char;
use std::ffi::CString;

#[no_mangle]
pub extern "C" fn hello() -> *const c_char {
    let string = match CString::new("RUST SAYS: Hello world!") {
        Ok(string) => string,
        Err(_) => panic!("Failed to create string"),
    };

    return string.as_ptr();
}

/// Expose the JNI interface for android below
#[cfg(target_os="android")]
#[allow(non_snake_case)]
pub mod android {
    use super::*;
    use jni_sys::*;
    use std::os::raw::{c_void};
    use std::ffi::CString;
    use std::ffi;

    #[no_mangle]
    pub unsafe extern "C" fn Java_com_example_rustflutter_RustActivity_hello(env: *mut JNIEnv, class: *const c_void) -> jstring {
        let ref java = **env;
        let string = hello();
        let string = unsafe { (java.NewStringUTF)(env, string) };

        return string;
    }
}

#[cfg(test)]
mod tests {
    use { hello };
    use std::ffi::CString;

    #[test]
    fn test_hello() {
        let expected = match CString::new("RUST SAYS: Hello world!") {
            Ok(string) => string.as_ptr(),
            Err(err) => panic!(err),
        };
        let actual = hello();
        assert_eq!(actual, expected);
    }
}
