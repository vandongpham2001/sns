const dbs = ["sns_notification", "sns_post", "sns_file", "sns_chat"];

dbs.forEach((dbName) => {
  const dbInstance = db.getSiblingDB(dbName);
  dbInstance.createCollection("init");
});
