package de.nightlife.restservice.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Artist
{
	private @Id @GeneratedValue Long id;
	private String name;

	public Artist( )
	{
	}

	public Artist( final String name )
	{
		this.name = name;
	}

	public Long getId( )
	{
		return id;
	}

	public void setId( final Long id )
	{
		this.id = id;
	}

	public String getName( )
	{
		return name;
	}

	public void setName( final String name )
	{
		this.name = name;
	}
}
