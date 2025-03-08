import React from "react";
import { Create, SimpleForm, TextInput, DateInput } from "react-admin";

const MovieCreate = () => (
  <Create>
    <SimpleForm>
      <TextInput source="title" />
      <TextInput source="genre" />
      <TextInput source="director" />
      <DateInput source="releaseDate" />
    </SimpleForm>
  </Create>
);

export default MovieCreate;
