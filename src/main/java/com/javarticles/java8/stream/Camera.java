package com.javarticles.java8.stream;

public class Camera {
	 // resolution is not optional - all cameras must have it
	  private Resolution resolution;
	 
	  public Resolution getResolution() {
	    return resolution;
	  }
	  /**
	   * Creating Optional objects:

Empty Optional: You can create an empty optional object use Optional.empty:
Optional<Phone> optPhone = Optional.empty();
Optional from a non-null value: To create an optional from a non-null value use Optional.of. Note that if the value is null a NullPointerException is thrown immediately:
Optional<Phone> optPhone = Optional.of(phone);
Optional from null: Use Optional.ofNullable to create an optional that may hold a null value. If the value is null, an empty optional is created.
Optional<Phone> optPhone = Optional.ofNullable(phone);
Optional<Camera> optCamera = Optional.ofNullable(camera);
Optional<Resolution> resolution = optCamera.map(Camera::getResolution);
Optional<Phone> optPhone = Optional.ofNullable(phone);
 
// calling map returns a two-level optional
Optional<Optional<Camera>> optOptCamera = optPhone.map(Phone::getCamera);
 
// but flatMap, returns a single-level optional
Optional<Camera> optCamera = optPhone.flatMap(Phone::getCamera);
public Resolution getPhoneCameraResolution(final Optional<Person> person) {
  return
    person.flatMap(Person::getPhone)  // returns Optional<Phone>
          .flatMap(Phone::getCamera) // returns Optional<Camera>
          .map(Camera::getResolution) // returns Optional<Resolution>
          .orElse(Resolution.UNKNOWN); // returns Resolution or UNKNOWN if not found
}
This code returns the resolution of the person's camera phone. If the person is null, doesn't have a phone, or the phone doesn't have a camera, an UNKNOWN resolution is returned.

/create an empty optional
Optional<Book> empty = Optional.empty();
//create an instance of a Book and wrap it inside an Optional
Optional<Book> bookOptional = Optional.of(new Book());
Optional<Book> bookOptional = findBook("The War of the Worlds");
Book book = bookOptional.orElse(new Book());
Optional<Book> bookOptional = findBook("The War of the Worlds");
Book book = bookOptional.orElseGet(Book::defaultBook);
he orElseGet() method expects a Supplier which you can implement with a lambda expression. In this example I provided a method reference which returns a default book.

With the ifPresent() method it is possible to execute a behavior if the optional contains a value.
bookOptional.ifPresent(System.out::println);
String name = bookOptional.map(Book::getName).orElse("Name not provided");
Indrek Ots bio photo
BY INDREK OTS
 MARCH 12, 2016
 MARCH 03, 2016
 0 COMMENTS
 TWEET
 LIKE
 +1
Problems with null

If you have had any experience with Java, then you probably have seen the NullPointerException which is thrown when an application tries to use a null reference in a case where an object is required. This can lead to superfluous if-statements checking to see if a reference is null or not.

Null references were implemented because they were the easiest method to implement the absence of a value. Tony Hoare, a British computer scientist, designer of the ALGOL W programming language and the inventor of null references called it his billion-dollar mistake.

I call it my billion-dollar mistake. It was the invention of the null reference in 1965. At that time, I was designing the first comprehensive type system for references in an object oriented language (ALGOL W). My goal was to ensure that all use of references should be absolutely safe, with checking performed automatically by the compiler. But I couldn’t resist the temptation to put in a null reference, simply because it was so easy to implement. This has led to innumerable errors, vulnerabilities, and system crashes, which have probably caused a billion dollars of pain and damage in the last forty years. - Tony Hoare

How to avoid NullPointerExceptions

The easiest answer is that you should never return a null. But that’s easier said than done. What should I return when there is no value to return? What if the library I’m using returns nulls?

Apparently null reference is not the best way to model an absence of a value. Instead you should return an object which represents an absence of a value. But wouldn’t that lead to superfluous if-else statements checking if this object contains a value or not? Bear with me until I show how Optionals can solve this problem. But first, let’s quickly go over how other languages have solved the same issue.

A lot of other languages have introduced a Maybe type - something that represents a sate where there might not be a value. For example, Scala has scala.Option and Standard ML uses option. Even if the language does not provide a concept of Maybe, there usually is a library that solves the problem. For instance, Guava, the popular Java library by Google, includes an Optional class.

Optional

Optional is a container object which may or may not contain a non-null value. It has many useful methods that make programming without null checks more convenient. Let’s look at a few examples.

The example domain contains books. Creating an optional is easy. The class provides static methods to do so.

//create an empty optional
Optional<Book> empty = Optional.empty();
//create an instance of a Book and wrap it inside an Optional
Optional<Book> bookOptional = Optional.of(new Book());
To get the contents of the Optional container, you can call the get() method on it. If you’re unsure if the container contains a non-null value, it is possible to use the isPresent() method. It returns true if it contains a non-null value. Be aware that checking the container for contents and then retrieving it defeats the purpose of Optionals in my opinion. In terms of superfluous if-statements, it is the same as checking if an object is null or not.

Instead I would advise you to look at the methods provided by the Optional class and see if you can come up with a more clever solution. The following are a few useful use cases where Optional is used.

Examples of Optional

Let’s look at some examples of Optionals in action. As previously mentioned, calling the get() method on an Optional, will return its contents. Instead of calling get() it’s better to use the orElse() method to which you can pass a value that will be returned if the Optional contains a null reference.

Optional<Book> bookOptional = findBook("The War of the Worlds");
Book book = bookOptional.orElse(new Book());
If the Optional contains a null reference, a new book is returned. Depending on the situation, returning a plain Book object might not desirable since all of it’s fields are empty. The API provides other useful methods as well.

Optional<Book> bookOptional = findBook("The War of the Worlds");
Book book = bookOptional.orElseGet(Book::defaultBook);
The orElseGet() method expects a Supplier which you can implement with a lambda expression. In this example I provided a method reference which returns a default book.

With the ifPresent() method it is possible to execute a behavior if the optional contains a value.

bookOptional.ifPresent(System.out::println);
Rather than retrieving a Book object, you can extract the instance fields from the Book object inside the Optional container. In the following example we get the name of the book. If the name is not present or there’s no book at all, a default value is returned.

String name = bookOptional.map(Book::getName).orElse("Name not provided");
The same method can be used if there’s a chain of objects.

public Flight confirmDepartureTime(Flight flight) {
    // lookup flight against external service

    // return new flight with confirmed departure
    // - OR -
    // return original flight with unconfirmed departure
}
Let’s assume further that the object which communicates with the external service 
returns a Flight object wrapped in an Optional. If for whatever reason the flight isn’t found,
 our flight service returns an empty Optional. From there, if we receive flight data from the external
  service, we use that data to construct a confirmed flight.
 Otherwise, we simply return the unconfirmed flight as is.
 
public Flight confirmDepartureTime(Flight unconfirmedFlight) {
    Optional<Flight> maybeFlight = 
        flightService.confirmDeparture(unconfirmedFlight);

    if (maybeFlight.isPresent()) {
        return Flight.withConfirmedDeparture(
            maybeFlight.get()
        );
    }

    return unconfirmedFlight;
}
he code above does its job, but in fact looks only slightly
 different from the usual null checking pattern. More importantly, the code above
  does not use the Optional object in its proper idiom.

Instead of calling isPresent, we can use two handy functions on the Optional class: 1) 
map and 2) orElse. Observe:
public Flight confirmDepartureTime(Flight unconfirmedFlight) {
    Optional<Flight> maybeFlight = 
        flightService.confirmDeparture(unconfirmedFlight);

    return maybeFlight
            .map(f -> {
                return Flight.withConfirmedDeparture(f);
            })
            .orElse(unconfirmedFlight);
}
The beautiful thing about Optional is that calling map on an empty Optional 
will be a no-op which immediately falls through to the orElse call.
 If there is a value inside maybeFlight, the map function will transform that value with the 
 lambda and skip orElse.
finally the equisite code 
public Flight confirmDepartureTime(Flight unconfirmedFlight) {
    return flightService
            .confirmDeparture(unconfirmedFlight)
            .map(Flight::withConfirmedDeparture)
            .orElse(unconfirmedFlight);
}
A container object which may or may not contain a non-null value. 
If a value is present, isPresent() will return true and get() will return the value.
/Creating an instance of Optional using the factory method.
Optional<String> name = Optional.of("Sanaulla");
//This fails with a NullPointerException.
Optional<String> someNull = Optional.of(null);
/This represents an instance of Optional containing no value
//i.e the value is 'null'
Optional empty = Optional.ofNullable(null);
/isPresent method is used to check if there is any 
//value embedded within the Optional instance.
if (name.isPresent()) {
  //Invoking get method returns the value present
  //within the Optaional instance.
  System.out.println(name.get());//prints Sanaulla
}
//The below code prints: No value present 
try {
  //Invoking get method on an empty Optaional instance 
  //throws NoSuchElementException.
  System.out.println(empty.get());
} catch (NoSuchElementException ex) {
  System.out.println(ex.getMessage());
}
//ifPresent method takes a lambda expression as a parameter.
//The lambda expression can then consume the value if it is present
//and perform some operation with it.
name.ifPresent((value) -> {
  System.out.println("The length of the value is: " + value.length());
});
//orElse method either returns the value present in the Optional instance
//or returns the message passed to the method in case the value is null.
//prints: There is no value present!
System.out.println(empty.orElse("There is no value present!"));
//prints: Sanaulla
System.out.println(name.orElse("There is some value!"));
//orElseGet is similar to orElse with a difference that instead of passing 
//a default value, we pass in a lambda expression which generates the default 
//value for us.
//prints: Default Value
System.out.println(empty.orElseGet(() -> "Default Value"));
//prints: Sanaulla
System.out.println(name.orElseGet(() -> "Default Value"));
try {
  //orElseThrow similar to orElse method, instead of returning a default
  //value, this method throws an exception which is generated from 
  //the lambda expression/method reference passed as a param to the method.
  empty.orElseThrow(ValueAbsentException::new);
} catch (Throwable ex) {
  //prints: No value present in the Optional instance
  System.out.println(ex.getMessage());
}
map

From the documentation for map method:

If a value is present, apply the provided mapping function to it, 
and if the result is non-null, return an Optional describing the result. Otherwise return an empty Optional.
This method is used to apply a set of operations on the value present in the Optional instance.
 The set of operations are passed in the form of a 
lambda expression representing an implementation of the Function interface
//map method modifies the value present within the Optional instance
//by applying the lambda expression passed as a parameter. 
//The return value of the lambda expression is then wrapped into another
//Optional instance.
Optional<String> upperName = name.map((value) -> value.toUpperCase());
System.out.println(upperName.orElse("No value found"));
flatMap

From the documentation for the flatMap method:

If a value is present, apply the provided Optional-bearing mapping function to it, 
return that result, otherwise return an empty Optional.
 This method is similar to map(Function), but the provided mapper is one
  whose result is already an Optional, and if invoked, flatMap does not wrap it with an additional Optional.
  his method is very similar to map method and differs from it in the return type of the mapping function passed to it. In the case of map method the mapping function return value can be of any type T, where as in case of flatMap method the return value of the mapping function can only be of type Optional.

/flatMap is exactly similar to map function, the differece being in the
//return type of the lambda expression passed to the method.
//In the map method, the return type of the lambda expression can be anything
//but the value is wrapped within an instance of Optional class before it 
//is returned from the map method, but in the flatMap method the return 
//type of lambda expression's is always an instance of Optional.
upperName = name.flatMap((value) -> Optional.of(value.toUpperCase()));
System.out.println(upperName.orElse("No value found"));//prints SANAULLA
filter

This method is used to restrict the value within an Optional instance by passing the
 condition to be applied on the value to the filter method. The documentation says:

If a value is present, and the value matches the given predicate, 
return an Optional describing the value, otherwise return an empty Optional.
/filter method is used to check if the given optional value satifies
//some condtion. If it satifies the condition then the same Optional instance
//is returned, otherwise an empty Optional instance is returned.
Optional<String> longName = name.filter((value) -> value.length() > 6);
System.out.println(longName.orElse("The name is less than 6 characters"));//prints Sanaulla
 
//Another example where the value fails the condition passed to the 
//filter method.
Optional<String> anotherName = Optional.of("Sana");
Optional<String> shortName = anotherName.filter((value) -> value.length() > 6);
//prints: The name is less than 6 characters
System.out.println(shortName.orElse("The name is less than 6 characters"));

	   */
}