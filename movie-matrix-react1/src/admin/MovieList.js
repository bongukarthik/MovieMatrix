import React from "react";
import { List, Datagrid, TextField, DateField, EditButton, DeleteButton } from "react-admin";

const MovieList = () => (
  <List>
    <Datagrid>
      <TextField source="id" />
      <TextField source="title" />
      <TextField source="genre" />
      <TextField source="director" />
      <DateField source="releaseDate" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

export default MovieList;
