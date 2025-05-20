// ===== Variables & Data Types =====

// 1. Variables Declaration
// let - can be reassigned
let name = "John";
name = "Jane"; // Valid

// const - cannot be reassigned
const age = 25;
// age = 26; // Error: Cannot reassign const

// var - old way (avoid using)
var city = "New York";

// 2. Primitive Data Types
// String
const greeting = "Hello World";
const name2 = 'John';
const template = `Hello ${name2}`; // Template literal

// Number
const count = 10;
const price = 99.99;
const bigNumber = 1e6; // 1,000,000

// Boolean
const isActive = true;
const isLoggedIn = false;

// 3. Objects
const user = {
  name: "John",
  age: 25,
  isActive: true,
  address: {
    city: "New York",
    country: "USA"
  }
};

// Accessing object properties
console.log(user.name); // "John"
console.log(user["age"]); // 25

// 4. Arrays
const fruits = ["apple", "banana", "orange"];
const numbers = [1, 2, 3, 4, 5];

// Accessing array elements
console.log(fruits[0]); // "apple"
console.log(numbers[2]); // 3

// 5. Type Checking
console.log(typeof name); // "string"
console.log(typeof age); // "number"
console.log(typeof isActive); // "boolean"
console.log(typeof user); // "object"
console.log(typeof fruits); // "object" (arrays are objects)

// 6. Type Conversion
const numString = "42";
const num = Number(numString); // Convert to number
const strNum = String(42); // Convert to string
const bool = Boolean(1); // Convert to boolean

// Practice Exercises:
// 1. Create a user object with your details
// 2. Create an array of your favorite movies
// 3. Try different type conversions 