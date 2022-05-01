# Unit Test Scenario Github User

## UserRepositoryTest

### `getUserDetail()`

- getDetailBy first item should resource loading
- getDetailBy should return loading and success when data is found in database
- getDetailBy should return loading and success when data is not found in database but found in
  network
- getDetailBy should return loading and error when data is not found in database and network

### `getFavorite()`

- getFavorite should return success when data is found in database
- getFavorite should return empty when data is not found in database
  
### `setFavorite()`
- setFavorite should change userEntity isFavorite true to userEntity isFavorite false
- setFavorite should change userEntity isFavorite false to userEntity isFavorite true

## LocalDataSourceTest

### `searchBy()`

- searchBy should return success when data is found
- searchBy should return empty when data is null or empty

### `getFavorite()`

- getFavorite should return success when data is found
- getFavorite should return empty when data is not found

### `getDetailBy()`

- getDetailBy should return success when data is found
- getDetailBy should return empty when data is not found

### `insertUsers()`
- insertUsers should insert data to database when list is not null
  
### `insertUserDetail()`
- insertUserDetail should insert data to database when user is not null
  
### `favorite()`
- favorite should change isFavorite to true and update to database
  
## `unFavorite()`
- unFavorite should change isFavorite to false and update to database

## RemoteDataSourceTest

- given username and position searchBy should return Response Success
- given empty username searchBy should return Response Error
- given position = 0 should searchBy return Response Error
- given empty username getDetailBy should return Response Error
- given username getDetailBy return Response Success

## UserPagingSourceTest

- loadReturnsPageWhenOnSuccessfulLoadOfItemKeyedData