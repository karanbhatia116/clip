# Clip

This application supports multiple features such as: 


- add expense ✅
- remove expense ✅
- settlement between users ✅
- update expense ✅
- create group ✅
- add another user to group ✅
- update group ✅
- delete group ✅
- simplify splits in the group ✅
- can check the total lent amount and borrowed amount among users and groups ✅
- can list/view all the groups a user is a part of ✅
- can list/view all expenses in a group ✅
- can check total owing amount in the group ✅
- can check transactions in the group where user is owed by or owing some amount to their friend ✅


# Todo Items
- [x] Refactor error handling by throwing custom exceptions and catching at interceptor
- [ ] Refactor expense addition
- [ ] Fix error handling for expenses
- [ ] Add support for automatic split calculation from backend without relying on client to provide splits
- [ ] Rollback changes in other entities (splits and transactions) when adding expense fails
- [ ] Remove auto incrementing long ids from databases (Major refactor). Start using UUIDs