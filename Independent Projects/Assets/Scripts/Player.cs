using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    private int health = 5;
    private int speed = 10; //player speed
    private bool fighting = false; //is the player currently throwing an attack? note: design choice, I wanted the player to only be able to throw one attack at a time
                                   //and not be able to end an attack an animation early. IE you have to wait for the punch animation to finish before throwing another.
    private bool facingRight = true; //is the player facing Right?
    private float dir = 1f; //1 = facing right, -1 = facing left. May delete later
    private float delay = 0.0f; //delay between character throwing an attack and it registering (see Fight coroutine)
    private float distance = 0.0f; //distance of Raycast sent out based on attack.
    private string move = ""; //current attack being thrown

    private Animator anim;
    private Rigidbody2D rb2d;

    // Update is called once per frame

    void Start()
    {
        anim = gameObject.GetComponent<Animator>();
        rb2d = gameObject.GetComponent<Rigidbody2D>();
    }
    void LateUpdate()
    {
        PlayerMove();
    }

    void PlayerMove()
    {
        float moveX = Input.GetAxis("Horizontal");

        if (moveX > 0.0f && !facingRight)
        {
            Flip();
            dir = 1f;
        } else if (moveX < 0.0f && facingRight)
        {
            Flip();
            dir = -1f;
        }

        if (Input.GetKeyDown(KeyCode.Space) && rb2d.velocity.y < 0.1f && rb2d.velocity.y > -.1f)
        {
            Jump();
        }

        if (Input.GetKeyDown(KeyCode.Q))
        {
            move = "Punch";
            delay = .25f;
            distance = 1f;
        } else if (Input.GetKeyDown(KeyCode.W))
        {
            move = "Uppercut";
            delay = .35f;
            distance = .9f; //will likely change later;
        }

        if (move != "" && !fighting)
        {
            StartCoroutine(Fight(move));
        }

        rb2d.velocity = new Vector2(moveX * speed, rb2d.velocity.y);

    }

    void Jump()
    {
        rb2d.AddForce(new Vector2(0, 20), ForceMode2D.Impulse);
    }

    void Flip()
    {
        facingRight = !facingRight;
        Vector2 temp = gameObject.transform.localScale;
        temp.x *= -1;
        gameObject.transform.localScale = temp;
    }

    void Damage()
    {
        health -= 1;
        if (health <= 0)
        {
            Destroy(gameObject);
        }
    }

    IEnumerator Fight(string name)
    {
        anim.Play(name);
        fighting = true;
        Debug.Log("Animation starting");

        yield return new WaitForSeconds(anim.GetCurrentAnimatorStateInfo(0).length * (float) delay);

        //Note to self : last number should change based on which attack is thrown
        RaycastHit2D hit = Physics2D.Raycast(transform.position, new Vector2(1, 0) * dir, (float)distance);

        if (hit.collider != null)
        {
            Debug.Log("Hit " + hit.collider.tag + " with " + name + "!");

            //NTS: Damage changes based on attack
            hit.collider.SendMessage("Damage", name);
        }

        yield return new WaitForSeconds(anim.GetCurrentAnimatorStateInfo(0).length);
        fighting = false;
        delay = 0.0f;
        move = "";
    }
}
