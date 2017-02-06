This project shows that promise linking is unnecessary complication and the 
problem of leaking memory can be fixed by simple extraction of closure to 
separate method to avoid accidentally closing over the Future creating function.
- futures.before.concurrent = scala.concurrent in commit 
  54cb6af7dbcf630a4f57e98f0099d77dd3b36693 (just before adding promise linking)
- futures.after.concurrent = scala.concurrent in commit 
  48c677ceb3177d93e700b399c00af6b8bb6419e4 (where promises were started to be 
  linked)
- futures.fixed = futures.before + feedPromise extracted helper function
