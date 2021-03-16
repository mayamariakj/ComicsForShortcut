# ComicsForShortcut

# Hi guys!
I’m Maya. a student from the Space-invader bachelor-team.
I really liked working on this task. I thought it was fun to (usually) get served a comic between each time I tried to run the application. Not gonna lie, there allso was a quite few (cough) errores as well.

I worked on the application in the evenings after bachelor thesis writing and work. I see that the application absolutely has potential for improvement but I am satisfied with the implementations I selected and I feel it represents a minimum viable product. 


![alt text](https://www.meme-arsenal.com/memes/c182f8dfcdf157a3704944c6a0d93310.jpg)

## The challenge.

A client of Shortcut had just discovered the xkcd comic and wanted an application to view the comics. She wants a minimum viable product that contains just enough features to be usable by an early customer. 
Even though she just wants a MVP this time, she has a list of requirements she wants to be implemented in the final app:

The list of the requirements are : 
* Browse through the comic,
* See the comics details, including its description,
* Search for comics by the comic number as well as text,
* Get the comics explanation
* Favourite the comics, which would be available offline too,
* Send comics to others,
* Get notifications when a new comic is published,
* Support multiple form factors.

## The requirements i chose for the MVP application

* Browsing through the comics. 
This is a must have in an application like this one. If you can't browse through them, you might as well buy a cartoon magazine. 

To be able to browse through the comics, I created a function called  `<getComicFromAPI>` that contacts the xkcd API and retrieves a `<ComicData>`-object based on the number for the comic: `<https://xkcd.com/ **$number** /info.0.json>`. The image-url from `<ComicData>` is then sent to Picasso and displays it on the main screen.

* See the comics details, including the description. 
I thought this would be a nice touch for the comic image to have a title and since the description sometimes is the whole punchline of the comic, it would of course have to be visible. 

* Send comics to others.
"In these days" even though we can't meet face to face and make eachother laugh, we can still send a little text or a link to force a smile on a friend's face.
Therefore I thought it would be nice to implement the "Send comics to others' '. Having a feature like this also helps with traffic on the website and would help the application get more downloads.

* Support multiple forms factors. 
The comics have different widths and lengths. Although Picasso helps me with the scaling, the aspect ratio of the comics are set, which means that the pictures sometimes can be very small. Therefore I think it's nice if the user is able to turn their phone to get a bigger picture of the comic. 

## What i could have done differently
 * Unfortunately I have no kind of error handling. If the user is without internet or there is an error in the API, the user is also left without an application or any notion of what's wrong. This is of course the first thing I would fix if I had more time.
 * I could have implemented a database for storing favourites. This would also help the application work offline and would help me with a better image transition. Right now the images are reloaded when the user presses the next button and I could have used the database as a buffer for a more seamless transition between images.
* Sometimes it is not enough to turn the phone for a better view of the image. I looked into how to be able to pinch the image to zoom, but it was more difficult than I thought and I ran out of time.

## Language. 

I was unsure if i should write the project in Kotli or in Swift. Personally, I think that it’s much easier to create a good looking app on iOS than it is on Android. This time my gut feeling told me to go for Kotlin. Why? I had a lower heart rate on my Kotlin exam than I had on my iOS exam, and I also figured that I could reuse some elements from that app in this one.

## Libraries for the code 

I use Fuel for the web-call to the API. It's easy to implement, and I have used it in nearly all apps at school.

Gson is my solution for converting the JSON that i receive from the xkcd-API into data-models for the application. 

Picasso takes care of the image-handling. I simply feed it an url, apply some formatting and it gives me images for every comic available. Picasso is a great example of an external library that "just works" and has great documentation. 
