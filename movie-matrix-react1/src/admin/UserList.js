import React from "react";
import { List, Datagrid, TextField, EmailField, EditButton, DeleteButton } from "react-admin";

const UserList = () => (
  <List>
    <Datagrid rowClick="edit">
      <TextField source="id" />
      <TextField source="name" label="Full Name" />
      <EmailField source="email" />
      <TextField source="role" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

export default UserList;
