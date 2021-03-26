# onepairv2
A “LIVE” discount matching application using Android Studio enabling users to view list of available categorized one-for-one discounts. Users can post and match with someone to enjoy discount.

## Objective
- Allow users to see available one-for-one deals
- Find another user to match with based on preferred location and deal
- Chatroom to make appropriate arrangements available once match occurs

## Main Features Implemented
- Log In/Register
- Display one-for-one deals for chosen category
- Find match
- Chat

## APIs used
- RESTful API
    - API to get scrapped data for deals <img src="/image/magnifying_glass.png" width="2.5%" height="2.5%">
- Firebase Api
    - API to store user data, session token and chat data. <img src="/image/book.png" width="2.5%" height="2.5%">
- Picasso Api
    - API to display images from image url <img src="/image/picture.png" width="2.5%" height="2.5%">
- Government Train Station API
    - API to retrieve locations with MRT Stations <img src="/image/map.png" width="2.5%" height="2.5%">
- ArthurHub's ANdroid Image Cropper
    - API to allow users to upload profile picture <img src="/image/human.png" width="2.5%" height="2.5%">

## Use Case Diagram
<img src="/image/use_case.png" width="100%" height="100%">

## Backend
- Diagram of Restful API (CRUD operation)
    - Create
    - Read
    - Update
    - Delete
- Example of implemented API Calls

### RESTful 
<img src="/image/restful.png" width="100%" height="100%">

### Examples of Implemented API Calls
```java
// Client Side example
public interface BackEndController {
    public static final String URL = "https://....";

    @GET("getAllDeals")
    Call<ArrayList<Deal>> getAllDeals();
}

```

```python
# Server Side example
def getAllDeals(request):
    querySet = Deals.objects.all()
    serializer = DealsSerializer(queryset, many = True)
    return HttpsResponse(json.dumps(serializer.data))

```

## Architecture Style

### 3-Tiered + Client Server Architecture
<img src="/image/architecture_style.png" width="100%" height="100%">


## Design Pattern Implemented

### Stratergy + Factory Pattern
- Problem: To resolve the issue of increased `dependencies` and `tight coupling`
<img src="/image/stratergy_factory.png" width="100%" height="100%">

### Observer Pattern
- Problem: To resolve the need of constnatly checking on the server for the match's result.
<img src="/image/observer.png" width="100%" height="100%">


## Project Scheduling

### Overall Planning
<img src="/image/overall_planning.png" width="100%" height="100%">

### Critical Path Analysis
<img src="/image/critical_path_analysis.png" width="100%" height="100%">


